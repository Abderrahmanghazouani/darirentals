package  ma.zyn.app.dao.criteria.core.client;


import ma.zyn.app.dao.criteria.core.enterprise.EnterpriseCriteria;

import ma.zyn.app.zynerator.security.dao.criteria.core.UserCriteria;

import java.util.List;

public class ClientCriteria extends UserCriteria  {

    private String fullName;
    private String fullNameLike;
    private String phone;
    private String phoneLike;
    private String nationality;
    private String nationalityLike;
    private String email;
    private String emailLike;
    private Boolean enabled;
    private Boolean credentialsNonExpired;
    private Boolean accountNonExpired;
    private String username;
    private String usernameLike;
    private Boolean passwordChanged;
    private Boolean accountNonLocked;
    private String password;
    private String passwordLike;

    private EnterpriseCriteria enterprise ;
    private List<EnterpriseCriteria> enterprises ;


    public String getFullName(){
        return this.fullName;
    }
    public void setFullName(String fullName){
        this.fullName = fullName;
    }
    public String getFullNameLike(){
        return this.fullNameLike;
    }
    public void setFullNameLike(String fullNameLike){
        this.fullNameLike = fullNameLike;
    }

    public String getPhone(){
        return this.phone;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public String getPhoneLike(){
        return this.phoneLike;
    }
    public void setPhoneLike(String phoneLike){
        this.phoneLike = phoneLike;
    }

    public String getNationality(){
        return this.nationality;
    }
    public void setNationality(String nationality){
        this.nationality = nationality;
    }
    public String getNationalityLike(){
        return this.nationalityLike;
    }
    public void setNationalityLike(String nationalityLike){
        this.nationalityLike = nationalityLike;
    }

    public String getEmail(){
        return this.email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmailLike(){
        return this.emailLike;
    }
    public void setEmailLike(String emailLike){
        this.emailLike = emailLike;
    }

    public Boolean getEnabled(){
        return this.enabled;
    }
    public void setEnabled(Boolean enabled){
        this.enabled = enabled;
    }
    public Boolean getCredentialsNonExpired(){
        return this.credentialsNonExpired;
    }
    public void setCredentialsNonExpired(Boolean credentialsNonExpired){
        this.credentialsNonExpired = credentialsNonExpired;
    }
    public Boolean getAccountNonExpired(){
        return this.accountNonExpired;
    }
    public void setAccountNonExpired(Boolean accountNonExpired){
        this.accountNonExpired = accountNonExpired;
    }
    public String getUsername(){
        return this.username;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsernameLike(){
        return this.usernameLike;
    }
    public void setUsernameLike(String usernameLike){
        this.usernameLike = usernameLike;
    }

    public Boolean getPasswordChanged(){
        return this.passwordChanged;
    }
    public void setPasswordChanged(Boolean passwordChanged){
        this.passwordChanged = passwordChanged;
    }
    public Boolean getAccountNonLocked(){
        return this.accountNonLocked;
    }
    public void setAccountNonLocked(Boolean accountNonLocked){
        this.accountNonLocked = accountNonLocked;
    }
    public String getPassword(){
        return this.password;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getPasswordLike(){
        return this.passwordLike;
    }
    public void setPasswordLike(String passwordLike){
        this.passwordLike = passwordLike;
    }


    public EnterpriseCriteria getEnterprise(){
        return this.enterprise;
    }

    public void setEnterprise(EnterpriseCriteria enterprise){
        this.enterprise = enterprise;
    }
    public List<EnterpriseCriteria> getEnterprises(){
        return this.enterprises;
    }

    public void setEnterprises(List<EnterpriseCriteria> enterprises){
        this.enterprises = enterprises;
    }
}
