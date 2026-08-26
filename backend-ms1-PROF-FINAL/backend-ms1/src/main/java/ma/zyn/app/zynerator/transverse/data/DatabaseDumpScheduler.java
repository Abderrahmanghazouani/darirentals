package ma.zyn.app.zynerator.transverse.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Desactive par defaut : ce scheduler pousse un dump de la base vers un depot Git distant
 * avec des identifiants places en placeholder dans application.properties
 * (remoteRepo.accessToken=yourAccessTokenPlz). @EnableScheduling a ete ajoute pour le nouveau
 * ExchangeRateSyncService (NOTES-devises.md) - sans ce garde-fou, ce scheduler se serait
 * reveille par effet de bord (il etait inerte jusqu'ici faute de @EnableScheduling). A activer
 * explicitement via database.dump.scheduler.enabled=true une fois de vrais identifiants
 * configures.
 */
@Component
@ConditionalOnProperty(name = "database.dump.scheduler.enabled", havingValue = "true")
public class DatabaseDumpScheduler {
    @Value("${spring.datasource.username}")
    private String dbUsername;
    @Value("${spring.datasource.password}")
    private String dbPassword;
    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Autowired
    private DumpUtil dumpUtil;
    @Autowired
    private PushUtil pushUtil;
    @Value("${remoteRepo.projectName}")
    private String projectnameInRemoteRepo;
    @Value("${remoteRepo.dumpFolder}")
    private String dumpFolder;
    @Value("${remoteRepo.shFolder}")
    private String shFolder;
    @Value("${remoteRepo.commitMessage}")
    private String commitMessage;
    @Value("${remoteRepo.username}")
    private String remoteRepoUsername;
    @Value("${remoteRepo.accessToken}")
    private String personalAccessToken;

    @Scheduled(cron = "0 */5 * * * ?")
    public void dumpDatabase() {
        dumpUtil.execFile();
        List<String> errors = pushUtil.createExecFile(dumpFolder, shFolder, projectnameInRemoteRepo, commitMessage, remoteRepoUsername, personalAccessToken);
        System.out.println("errors = " + errors);
    }
}
