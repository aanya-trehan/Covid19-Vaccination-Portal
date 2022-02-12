package AP;

import java.util.ArrayList;
import java.util.Scanner;

public class main {

    static ArrayList<Long> hospitalIDS=new ArrayList<>();
    static ArrayList<String> citizen_IDS=new ArrayList<>();
    static ArrayList<String> vaccineNames=new ArrayList<>();
    static ArrayList<hospital> hospitalList=new ArrayList<>();
    static ArrayList<slots> slotList=new ArrayList<>();
    static ArrayList<citizen> citizenList=new ArrayList<>();
    static ArrayList<vaccine> vaccineList=new ArrayList<>();
    private static int hosp_count=0;

    public static void main(String[] args) {
        System.out.println();
        System.out.println(" CoWin Portal initialised...");
        Scanner scan = new Scanner(System.in);
        boolean breakYN=false;
        while (!breakYN)
        {
            //MENU
            System.out.println();
            System.out.println("----------------------------------");
            System.out.println("1.Add Vaccine");
            System.out.println("2.Register Hospital");
            System.out.println("3.Register Citizen");
            System.out.println("4.Add Slot for Vaccination");
            System.out.println("5.Book Slot for Vaccination");
            System.out.println("6.List all slots for a hospital");
            System.out.println("7.Check Vaccination Status");
            System.out.println("8.Exit");
            System.out.println("----------------------------------");
            int option = scan.nextInt();

            //-----------------OPTION 8-------------------------
            if (option==8){
                breakYN=true;
            }

            //-----------------OPTION 1-------------------------
            else if (option==1)
            {
                System.out.print("Vaccine Name:");
                String vaccine_name=scan.next();
                if (vaccineNames.contains(vaccine_name)){
                    System.out.println("Vaccine already exists");
                    continue;
                }
                System.out.print("Number of Doses:");
                long number_of_doses=scan.nextLong();
                if(number_of_doses>1)
                {
                    System.out.print("Gap between Doses:");
                    long vaccine_gap=scan.nextLong();
                    vaccine newVaccine= new vaccine(vaccine_name,number_of_doses,vaccine_gap);
                    vaccineNames.add(vaccine_name);
                    vaccineList.add(newVaccine);
                    newVaccine.displayInfo();
                    continue;
                }
                vaccine newVaccine= new vaccine(vaccine_name,1,0);
                vaccineNames.add(vaccine_name);
                vaccineList.add(newVaccine);
                System.out.println(newVaccine.getDoseCount());
                newVaccine.displayInfo();
//                System.out.println(newVaccine);
            }

            //-----------------OPTION 2-------------------------
            else if (option==2)
            {
                //maybe hashmaps for searching slot
                System.out.print("Hospital Name:");
                String hospital_name=scan.next();
                System.out.print("Pincode:");
                String hospital_pincode=scan.next();
                if (hospital_pincode.length()!=6){
                    System.out.println("Invalid: Pincode must be 6 digits");
                    continue;
                }
                hosp_count++;
                long hospital_ID=100000+hosp_count;
                hospitalIDS.add(hospital_ID);
                hospital newHospital= new hospital(hospital_name,hospital_pincode,hospital_ID);
                hospitalList.add(newHospital);
                newHospital.displayInfo();
            }
            //-----------------OPTION 3-------------------------
            else if(option==3)
            {
                System.out.print("Citizen Name: ");
                String citizen_name=scan.next();
                System.out.print("Age:");
                long citizen_age =scan.nextLong();
                System.out.print("Unique ID:");
                String uniqueID=scan.next();
                if (uniqueID.length()!=12)
                {
                    System.out.println("Invalid: Unique ID must be of 12 digits");
                    continue;
                }
                else if (citizen_IDS.contains(uniqueID))
                {
                    System.out.println("A citizen with Unique ID:"+uniqueID+" is already registered");
                    continue;
                }

                if (citizen_age>=18)
                {
                    citizen newCitizen= new citizen(citizen_name,citizen_age,uniqueID);
                    citizen_IDS.add(uniqueID);
                    citizenList.add(newCitizen);
                    newCitizen.setVaccineStatus();
                    newCitizen.displayInfo();

                }
                else
                {
                    citizen newCitizen= new citizen(citizen_name,citizen_age,uniqueID);
                    citizen_IDS.add(uniqueID);
                    newCitizen.displayInfo();
                    System.out.println("Only above 18 are allowed");
                }
            }

            //-----------------OPTION 4-------------------------

            else if (option==4)
            {
                System.out.print("Enter Hospital ID:");
                long hospital_ID=scan.nextLong();
                if (!hospitalIDS.contains(hospital_ID))
                {
                    System.out.println("Invalid: Hospital ID "+hospital_ID+" is not registered");
                    System.out.println("Please register the hospital first");

                }
                else {
                    System.out.print("Enter number of slots to be added:");
                    int slots = scan.nextInt();
                    int count = 0;
                    while (count < slots) {
                        count++;
                        System.out.print("Enter Day Number:");
                        long day_number = scan.nextLong();
                        System.out.print("Enter quantity:");
                        long quantity = scan.nextLong();
                        System.out.println("Select Vaccine");
                        int index;
                        for (index = 0; index < vaccineNames.size(); index++) {
                            System.out.println(" " + index + ". " + vaccineNames.get(index));
                        }
                        //print out vaccine options
                        int selected_vaccine = scan.nextInt();
                        if (selected_vaccine > index) {
                            System.out.println("Invalid Code");

                        } else {
                            String hospitalName = null;
                            for (hospital h1 : hospitalList) {
                                if (h1.getUniqueID() == (hospital_ID)) {
                                    hospitalName = h1.getName();
                                    break;
                                }
                            }
                            slots newslot = new slots(hospitalName, hospital_ID, day_number, quantity, vaccineList.get(selected_vaccine));
                            newslot.displayInfo();
                            slotList.add(newslot);
                        }
                    }
                }
            }
            //-----------------OPTION 5-------------------------
            else if (option==5)
            {
                System.out.print("Enter patient unique ID:");
                String uniqueID=scan.next();
                if (uniqueID.length()!=12){
                    System.out.println("Invalid: ID should have 12 digits");
                    continue;
                }
                else if(!citizen_IDS.contains(uniqueID)) {
                    System.out.println("Invalid: This ID has not been registered");
                    continue;
                }

                String citizenName = null;
                citizen currentCitizen=null;
                for(citizen c:citizenList){
                    if (c.getUniqueID().equals(uniqueID)){
                        currentCitizen=c;
                        citizenName=c.getCitizenName();
                        break;
                    }

                }
                if (currentCitizen.getVaccineStatus().equals("FULLY VACCINATED")){
                    System.out.println(citizenName+" is already fully vaccinated");
                    continue;
                }

                System.out.println("1.Search by area");
                System.out.println("2.Search by Vaccine");
                System.out.println("3.Exit");
                System.out.print("Enter option:");
                int option_=scan.nextInt();
                if (option_==1)
                {
                    System.out.print("Enter PinCode:");
                    String pincode=scan.next();
                    boolean available=false;
                    for(hospital h:hospitalList){
                        if(h.getPincode().equals(pincode)){
                            available=true;
                            System.out.println(h.getUniqueID()+" "+h.getName());
                        }
                    }
                    if (!available){
                        System.out.println("No hospitals Available");
                        continue;
                    }

                    System.out.print("Enter hospital id:");
                    long hosp_id=scan.nextLong();
                    ArrayList<slots> hospSlots=new ArrayList<>();
                    int index=0;
                    long nextDue=0;
                    if (currentCitizen.getVaccineStatus().equals("PARTIALLY VACCINATED")) {
                        nextDue = currentCitizen.getDateVaccineGiven() + currentCitizen.vaccineGiven.getDoseGap();
                    }

                    available=false;
                    for (slots h: slotList){
                        if(h.getHospitalID()==hosp_id && h.getQty()>0 && (h.getDayNumber())>=(nextDue)){
                            available=true;
                            hospSlots.add(h);
                            System.out.println(index + "->Day:" + h.getDayNumber() + " Available Qty:" + h.getQty() + " Vaccine:" + h.getVaccineName());
                            index++;
                        }
                    }
                    if (!available){
                        System.out.println("No slots available");
                        continue;
                    }
                    System.out.print("Choose Slot:");
                    int slot_c=scan.nextInt();
                    slots currentSlot=hospSlots.get(slot_c);

                    if (currentCitizen.getVaccineStatus().equals("PARTIALLY VACCINATED")){
                        if(currentSlot.getDayNumber() < nextDue){
                            long vaxDate=currentCitizen.getDateVaccineGiven()+currentCitizen.vaccineGiven.getDoseGap();
                            System.out.println("Invalid: Vaccination Date is not due till "+vaxDate+ " day");
                            continue;
                        }
                        else{
                            System.out.println(citizenName+" vaccinated with "+ currentSlot.getVaccineName());
                            currentSlot.setQty();
                            currentCitizen.setVaccineDosesGiven();
                            currentCitizen.setDateVaccineGiven(currentSlot.getDayNumber());
                            currentCitizen.setVaccineStatus();
                            continue;
                        }
                    }

                    System.out.println(citizenName+" vaccinated with "+ currentSlot.getVaccineName());
                    for(vaccine v:vaccineList){
                        if(v.getVaccineName().equals(currentSlot.getVaccineName())){
                            currentCitizen.setVaccineGiven(v);
                        }
                    }
                    currentSlot.setQty();
                    currentCitizen.setVaccineDosesGiven();
                    currentCitizen.setVaccineStatus();
                    currentCitizen.setDateVaccineGiven(currentSlot.getDayNumber());


                }
                else if (option_==2)
                {
                    System.out.print("Enter Vaccine name:");

                    String vaccine_name=scan.next();

                    boolean available=false;
                    for(slots s:slotList){
                        if(s.getVaccineName().equals(vaccine_name)){
                            available=true;
                            System.out.println(s.getHospitalID()+" "+s.getHospitalName());
                        }
                    }
                    if (!available){
                        System.out.println("No hospitals Available");
                        continue;
                    }
                    System.out.print("Enter hospital id:");
                    long hosp_ID=scan.nextLong();
                    long nextDue=0;
                    if (currentCitizen.getVaccineStatus().equals("PARTIALLY VACCINATED")) {
                        nextDue = currentCitizen.getDateVaccineGiven() + currentCitizen.vaccineGiven.getDoseGap();
                    }

                    ArrayList<slots> hospSlots=new ArrayList<>();
                    int index=0;
                    available=false;

                    for (slots h: slotList){
                        if(h.getHospitalID()==hosp_ID && h.getQty()>0 && (h.getDayNumber())>=(nextDue) && h.getVaccineName().equals(vaccine_name)){
                            available=true;
                            hospSlots.add(h);
                            System.out.println(index + "->Day:" + h.getDayNumber() + " Available Qty:" + h.getQty() + " Vaccine:" + h.getVaccineName());
                            index++;
                        }
                    }
                    if (!available){
                        System.out.println("No slots available");
                    }
                    else {
                        System.out.print("Choose Slot:");
                        int slot_c = scan.nextInt();
                        slots currentSlot=hospSlots.get(slot_c);

                        if (currentCitizen.getVaccineStatus().equals("PARTIALLY VACCINATED")){
//                            System.out.println(currentSlot.getDayNumber()-currentCitizen.getDateVaccineGiven());
//                            System.out.println(nextDue);
                            if(currentSlot.getDayNumber() < nextDue){
                                long vaxDate=currentCitizen.getDateVaccineGiven()+currentCitizen.vaccineGiven.getDoseGap();
                                System.out.println("Invalid: Vaccination Date is not due till "+vaxDate+ " day");
                                continue;
                            }
                            else{
                                System.out.println(citizenName+" vaccinated with "+ currentSlot.getVaccineName());
                                currentSlot.setQty();
                                currentCitizen.setVaccineDosesGiven();
                                currentCitizen.setDateVaccineGiven(currentSlot.getDayNumber());
                                currentCitizen.setVaccineStatus();
                                continue;
                            }
                        }

                        System.out.println(citizenName+" vaccinated with "+ currentSlot.getVaccineName());
                        for(vaccine v:vaccineList){
                            if(v.getVaccineName().equals(currentSlot.getVaccineName())){
                                currentCitizen.setVaccineGiven(v);
                            }
                        }
                        currentSlot.setQty();
                        currentCitizen.setVaccineDosesGiven();
                        currentCitizen.setDateVaccineGiven(currentSlot.getDayNumber());
                        currentCitizen.setVaccineStatus();
                    }
                }
                else if (option_==3)
                {
                    breakYN=true;
                    break;

                }
                else
                {
                    System.out.println("Invalid: Option does not exist");

                }


            }

            //-----------------OPTION 6-------------------------

            else if (option==6)
            {
                System.out.print("Enter Hospital id:");
                long hosp_id=scan.nextLong();
                for (slots h: slotList){
                    if(h.getHospitalID()==hosp_id){
                        System.out.println("Day:"+h.getDayNumber()+" Vaccine: "+h.getVaccineName() + " Available Qty:"+ h.getQty());
                    }
                }

            }

            //-----------------OPTION 7-------------------------

            else if (option==7)
            {

                System.out.print("Enter Patient ID:");
                String patientID=scan.next();
                if (!(citizen_IDS.contains(patientID))){
                    System.out.println("No citizen Registered with this ID");
                    continue;
                }
                citizen currentCitizen=null;
                for (citizen c:citizenList){
                    if (c.getUniqueID().equals(patientID)){
                        currentCitizen=c;
                        break;
                    }
                }
                String status=currentCitizen.getVaccineStatus();
                if (status.equals("REGISTERED")){
                    System.out.println("Citizen REGISTERED");
                }
                else {
                    System.out.println(status);
                    System.out.println("Vaccine Given:" + currentCitizen.vaccineGiven.getVaccineName());
                    System.out.println("Number of Doses Given:" + currentCitizen.getVaccineDosesGiven());
                    if (status.equals("PARTIALLY VACCINATED")) {
                        long due = currentCitizen.getDateVaccineGiven() + currentCitizen.vaccineGiven.getDoseGap();
                        System.out.println("Next Dose due date:" + due);
                    }
                }

            }

            //-----------------OPTIONS END-------------------------
            else
            {
                System.out.println("Invalid option entered: Option does not exist in Menu.");

            }





        }
    }

}

class slots{


    //private final String HospitalName;
    private vaccine Vaccine;
    private final long hospitalID;
    private final long dayNumber;
    private long qty;
    private final String hospitalName;

    public slots(String hospitalName,long hospitalID,long dayNumber, long qty, vaccine Vaccine ){
        this.hospitalName=hospitalName;
        this.hospitalID=hospitalID;
        this.dayNumber=dayNumber;
        this.qty=qty;
        this.Vaccine=Vaccine;
    }

    public String getHospitalName() {
        return this.hospitalName;
    }

    public void setQty() {
        this.qty = qty-1;
    }

    public Long getHospitalID() {
        return this.hospitalID;
    }

    public Long getDayNumber() {
        return this.dayNumber;
    }

    public Long getQty() {
        return this.qty;
    }

    public String getVaccineName() {
        return this.Vaccine.getVaccineName();
    }

    public void displayInfo() {
        System.out.println("Slot Added by Hospital "+getHospitalID()+" for Day: "+getDayNumber()+", Available Quantity:"+getQty()+" of Vaccine "+getVaccineName());
    }
}

class citizen{

    private final String citizenName;
    private final long citizenAge;
    private final String uniqueID;
    //private  String vaccineName;
    private String vaccineStatus=null;
    //private long VaccineGap=0;
    private long VaccineDosesGiven=0;
    private long dayVaccinated;
    private vaccine vaccineGiven;


    public citizen(String citizenName, long citizenAge,String uniqueID)
    {
        this.citizenAge=citizenAge;
        this.citizenName=citizenName;
        this.uniqueID=uniqueID;
    }

//    public long getTotalDoses() {
//        return totalDoses;
//    }
//
//    public void setTotalDoses(long totalDoses) {
//        this.totalDoses = totalDoses;
//    }

    public String getCitizenName() {
        return citizenName;
    }

    public String getUniqueID() {
        return uniqueID;
    }

//    public long getCitizenAge() {
//        return citizenAge;
//    }

    public void displayInfo(){
        System.out.println("Citizen Name: "+getCitizenName()+", Age:"+ getCitizenAge()+", Unique ID:" + getUniqueID());
    }

    private long getCitizenAge() {
        return this.citizenAge;
    }

//    public long getVaccineGap() {
//        return VaccineGap;
//    }

    public long getVaccineDosesGiven() {
        return VaccineDosesGiven;
    }

    public String getVaccineStatus() {
        return vaccineStatus;
    }

    public void setVaccineStatus() {
        if(vaccineStatus==null){
            this.vaccineStatus="REGISTERED";
        }
        else if (this.vaccineGiven.getDoseCount()>this.VaccineDosesGiven){
            if (!(this.vaccineStatus.equals("PARTIALLY VACCINATED"))) {
                this.vaccineStatus = "PARTIALLY VACCINATED";
            }
        }
        else if (this.vaccineGiven.getDoseCount()==this.VaccineDosesGiven){
            this.vaccineStatus = "FULLY VACCINATED";
        }
    }

//    public void setFirstRegistration(){
//        this.vaccineStatus="REGISTERED";
//    }

    public void setVaccineDosesGiven() {
        VaccineDosesGiven++;
    }

    public Long getDateVaccineGiven() {
        return this.dayVaccinated;
    }
    public void setDateVaccineGiven(long dayVaccinated) {
        this.dayVaccinated=dayVaccinated;

    }
    public void setVaccineGiven(vaccine vaccineGiven) {
        this.vaccineGiven = vaccineGiven;
    }

}


class hospital{
    //private static ArrayList<slots> hospitalSlots=new ArrayList<>();
    private final String hospitalName;
    private final String hospitalPincode;
    private final long uniqueID;

    public hospital(String hospitalName,String hospitalPincode,long uniqueID){
        this.hospitalName=hospitalName;
        this.hospitalPincode=hospitalPincode;
        this.uniqueID=uniqueID;
    }
    public String getName() {
        return this.hospitalName;
    }
    public String getPincode() {
        return this.hospitalPincode;
    }
    public long getUniqueID() {
        return this.uniqueID;
    }

    public void displayInfo(){
        System.out.print("Hospital Name: " + getName() + " ,Pincode:"+getPincode()+" ,Unique ID:"+getUniqueID());
    }

}
class vaccine{
    private final String vaccineName;
    private final long doseCount;
    private final long doseGap;

    public vaccine(String vaccine_name,long dose_count, long dose_gap){
        this.vaccineName=vaccine_name;
        this.doseCount=dose_count;
        this.doseGap=dose_gap;
    }
    public String getVaccineName() {
        return this.vaccineName;
    }
    public long getDoseCount() {
        return this.doseCount;
    }
    public long getDoseGap() {
        return this.doseGap;
    }
    public void displayInfo(){
        System.out.print("Vaccine Name:"+this.getVaccineName()+ " ,Number of Doses:"+this.getDoseCount()+", Gap Between Doses:"+this.getDoseGap());
    }
}
