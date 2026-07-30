# Backend DariRentals — Version finale corrigée

Ce backend est le code généré par Zynerator (respectant fidèlement le schéma
YAML du CDC — 38 entités, tous les rôles/menus/state définis par le prof),
avec les corrections de bugs de génération nécessaires pour qu'il compile
et démarre. **Aucune logique métier n'a été modifiée ni retirée** — seuls
les noms de champs incohérents ont été corrigés pour correspondre à ce que
le schéma YAML définissait réellement.

## Cause racine (identique dans les 3 catégories de correctifs)

Le générateur a une limitation : quand une relation porte un **nom
personnalisé différent du nom du type** (ex: `Task.assignedTo` au lieu de
`Task.collaborator`), ou quand une entité a **plusieurs relations vers le
même type** (ex: `ExchangeRate.baseCurrency` ET `.targetCurrency`, toutes
deux de type `Currency`), le générateur a émis du code utilisant le nom
générique du type au lieu du vrai nom de champ, à plusieurs endroits :

### 1. Mappings JPA (`bean/core/**/*.java`) — 7 corrections
Annotations `@OneToMany(mappedBy = "...")` pointant vers un nom de champ
inexistant dans l'entité cible :
- `Collaborator.tasks` → `mappedBy="assignedTo"` (au lieu de `"collaborator"`)
- `Collaborator.reservationRequests` → `mappedBy="reviewedBy"`
- `Currency.exchangeRatesAsBase` → `mappedBy="baseCurrency"`
- `Currency.exchangeRatesAsTarget` → `mappedBy="targetCurrency"`
- `Currency.collaborators` → `mappedBy="displayCurrency"`
- `Property.reservationRequests` → `mappedBy="requestedProperty"`
- `Property.alternativeRequests` → `mappedBy="alternativeProperty"`

*(Sans ce correctif, l'application ne démarre pas du tout — erreur Hibernate
au chargement du contexte Spring.)*

### 2. Convertisseurs DTO (`ws/converter/**/*.java`) — 14 corrections
Appels `xxxConverter.setNomDuType(boolean)` corrigés vers le vrai nom de
champ (ex: `exchangeRateConverter.setCurrency(false)` →
`.setBaseCurrency(false)` + `.setTargetCurrency(false)`), dans
`CurrencyConverter`, `CollaboratorConverter`, `PropertyConverter`.

### 3. Services métier (`service/impl/**/*.java`) — 9 fichiers
Appels `xxxService.findByNomDuTypeId(...)` / `deleteByNomDuTypeId(...)` /
`.setNomDuType(...)` corrigés vers les vraies méthodes générées par le DAO
(ex: `findByCollaboratorId` → `findByAssignedToId`), dans les variantes
admin/client/collaborator de `CurrencyServiceImpl`, `CollaboratorServiceImpl`,
`PropertyServiceImpl`.

## Autre correctif : CORS

Ajout d'une configuration CORS (`WebSecurityConfig.java`) — absente du
générateur — pour autoriser le frontend Next.js (`localhost:3000`) à
appeler l'API (`localhost:8036`). Sans ça, le navigateur bloque toutes les
requêtes silencieusement.

## Autre correctif : spam de logs Kafka

`KafkaService.java` contient un `@KafkaListener` d'exemple (laissé par le
template du générateur, sans rapport avec les fonctionnalités du CDC). La
propriété `spring.kafka.enabled=false` dans `application.properties` ne
fait **rien** — ce n'est pas une propriété Spring standard, et rien dans le
code ne la lisait. Résultat : le listener essayait de se connecter à
`localhost:29092` en boucle infinie au démarrage, polluant les logs.

Ajout de `@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue
= "true")` sur `KafkaService` pour que cette propriété soit enfin respectée.

## Comptes de test (créés automatiquement au premier démarrage)

| Rôle | Username | Mot de passe |
|---|---|---|
| Admin | `admin` | `123` |
| Collaborateur | `collaborator` | `123` |
| Client | `client` | `123` |

⚠️ Le bloc de création (`AppApplication.java`, `if(true){...}`) s'exécute à
CHAQUE démarrage. Pense à le passer en `if(false){...}` une fois les
comptes créés, pour éviter les doublons en base à chaque redémarrage.

## Vérification effectuée

Un scan automatique de l'intégralité du code (mappings JPA, convertisseurs,
services) confirme **0 incohérence restante** de ce type. Le schéma de 38
entités correspond exactement au YAML `dari_schema.yaml` fourni par le CDC.
