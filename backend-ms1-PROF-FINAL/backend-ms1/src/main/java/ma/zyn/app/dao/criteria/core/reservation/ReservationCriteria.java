package  ma.zyn.app.dao.criteria.core.reservation;


import ma.zyn.app.dao.criteria.core.property.PropertyCriteria;
import ma.zyn.app.dao.criteria.core.client.ClientCriteria;

import ma.zyn.app.zynerator.criteria.BaseCriteria;

import java.util.List;

public class ReservationCriteria extends  BaseCriteria  {

    private String reference;
    private String referenceLike;
    private String amount;
    private String amountMin;
    private String amountMax;
    private String pricePerNight;
    private String pricePerNightMin;
    private String pricePerNightMax;

    private ClientCriteria client ;
    private List<ClientCriteria> clients ;
    private PropertyCriteria property ;
    private List<PropertyCriteria> propertys ;
    private ReservationPlatformCriteria reservationPlatform ;
    private List<ReservationPlatformCriteria> reservationPlatforms ;
    private ReservationStatusCriteria reservationStatus ;
    private List<ReservationStatusCriteria> reservationStatuss ;


    public String getReference(){
        return this.reference;
    }
    public void setReference(String reference){
        this.reference = reference;
    }
    public String getReferenceLike(){
        return this.referenceLike;
    }
    public void setReferenceLike(String referenceLike){
        this.referenceLike = referenceLike;
    }

    public String getAmount(){
        return this.amount;
    }
    public void setAmount(String amount){
        this.amount = amount;
    }   
    public String getAmountMin(){
        return this.amountMin;
    }
    public void setAmountMin(String amountMin){
        this.amountMin = amountMin;
    }
    public String getAmountMax(){
        return this.amountMax;
    }
    public void setAmountMax(String amountMax){
        this.amountMax = amountMax;
    }
      
    public String getPricePerNight(){
        return this.pricePerNight;
    }
    public void setPricePerNight(String pricePerNight){
        this.pricePerNight = pricePerNight;
    }   
    public String getPricePerNightMin(){
        return this.pricePerNightMin;
    }
    public void setPricePerNightMin(String pricePerNightMin){
        this.pricePerNightMin = pricePerNightMin;
    }
    public String getPricePerNightMax(){
        return this.pricePerNightMax;
    }
    public void setPricePerNightMax(String pricePerNightMax){
        this.pricePerNightMax = pricePerNightMax;
    }
      

    public ClientCriteria getClient(){
        return this.client;
    }

    public void setClient(ClientCriteria client){
        this.client = client;
    }
    public List<ClientCriteria> getClients(){
        return this.clients;
    }

    public void setClients(List<ClientCriteria> clients){
        this.clients = clients;
    }
    public PropertyCriteria getProperty(){
        return this.property;
    }

    public void setProperty(PropertyCriteria property){
        this.property = property;
    }
    public List<PropertyCriteria> getPropertys(){
        return this.propertys;
    }

    public void setPropertys(List<PropertyCriteria> propertys){
        this.propertys = propertys;
    }
    public ReservationPlatformCriteria getReservationPlatform(){
        return this.reservationPlatform;
    }

    public void setReservationPlatform(ReservationPlatformCriteria reservationPlatform){
        this.reservationPlatform = reservationPlatform;
    }
    public List<ReservationPlatformCriteria> getReservationPlatforms(){
        return this.reservationPlatforms;
    }

    public void setReservationPlatforms(List<ReservationPlatformCriteria> reservationPlatforms){
        this.reservationPlatforms = reservationPlatforms;
    }
    public ReservationStatusCriteria getReservationStatus(){
        return this.reservationStatus;
    }

    public void setReservationStatus(ReservationStatusCriteria reservationStatus){
        this.reservationStatus = reservationStatus;
    }
    public List<ReservationStatusCriteria> getReservationStatuss(){
        return this.reservationStatuss;
    }

    public void setReservationStatuss(List<ReservationStatusCriteria> reservationStatuss){
        this.reservationStatuss = reservationStatuss;
    }
}
