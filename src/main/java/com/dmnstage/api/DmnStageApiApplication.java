package com.dmnstage.api;

import com.dmnstage.api.entities.*;
import com.dmnstage.api.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class DmnStageApiApplication implements CommandLineRunner {

    private final IService service;

    @Autowired
    public DmnStageApiApplication(IService service) {
        this.service = service;
    }

    public static void main(String[] args) {
        SpringApplication.run(DmnStageApiApplication.class, args);
        /*String path;
        String pathFormat="api.dmnstage.com/img/#Product#-#Subproduct#-#Year#-#Month#-#Day#.jpg";
        pathFormat=pathFormat.replace("#Category#",Subproduct.pro)*/
    }

    @Override
    @Transactional
    public void run(String... args) {

        /*try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        //service.setConfigaasqs(new Configaasqs("pathFormat","api.dmnstage.com/img/#Product#-#Subproduct#-#Year#-#Month#-#Day#.jpg"));

        User admin1 = service.newUser(new Admin("AbdellahASKI", "654321", "Abdellah@aski.me", "+212707970909", "Abdellah", "ASKI"));
        User admin2 = service.newUser(new Admin("kumohira", "654321", "youssef@naitsaid.me", "+21200000000", "Youssef", "NAIT SAID"));

        //User U = service.getAdminById(1);
        //System.out.println(new BCryptPasswordEncoder().matches("654321", U.getPassword()));

        User client1 = service.newUser(new Client("client1", "654321", "client1@client1.com", "+21200000000", "Client1"));
        User client2 = service.newUser(new Client("client2", "654321", "client2@client2.com", "+21200000000", "Client2"));

        // Category teledetection = service.newCategory(new Category("Teledetection", "extranet.marocmeteo.ma/samba/detections/#Product#/#SUBProduct#/#Month##Day#/#SUBProduct#_#Year##Month##Day##Hour##Minute#.jpg"));
        //"http://extranet.marocmeteo.ma/samba/detections/"

        Product satelliteStandard = service.newProduct(new Product("Satellite Standard", "sats"));
        Product satelliteDeveloppe = service.newProduct(new Product("Satellite Developpe", "rgbs"));
        Product radaStandard = service.newProduct(new Product("Radar Standard", "radars"));
        Product radarDeveloppe = service.newProduct(new Product("Radar Developpe", "srmats"));
        Product foudreStandard = service.newProduct(new Product("Foudre Standard", "foudres"));

        service.mergeClientProduct((Client) client1, satelliteStandard);
        service.mergeClientProduct((Client) client1, satelliteDeveloppe);
        service.mergeClientProduct((Client) client1, radaStandard);
        service.mergeClientProduct((Client) client2, radarDeveloppe);
        service.mergeClientProduct((Client) client2, foudreStandard);

        SubProduct canalIR = service.newSubProduct(new SubProduct("Canal IR", "ir")); //image name : nothing
        SubProduct canalVS = service.newSubProduct(new SubProduct("Canal VS", "vs")); //image name : nothing
        SubProduct canalWV = service.newSubProduct(new SubProduct("Canal WV", "wv")); //image name : nothing
        SubProduct canalIRNB = service.newSubProduct(new SubProduct("Canal IR NB", "nbir")); //image name : nothing
        SubProduct canalHRV = service.newSubProduct(new SubProduct("Canal HRV", "hrv")); //image name : nothing

        service.addSubProductToProduct(canalIR, satelliteStandard);
        service.addSubProductToProduct(canalVS, satelliteStandard);
        service.addSubProductToProduct(canalWV, satelliteStandard);
        service.addSubProductToProduct(canalIRNB, satelliteStandard);
        service.addSubProductToProduct(canalHRV, satelliteStandard);

        SubProduct pouvoirPrecipitant = service.newSubProduct(new SubProduct("Pouvoir Precipitant", "pouvoir_precipitant")); //pvr_prec
        SubProduct bispectrale = service.newSubProduct(new SubProduct("Bispectrale", "bispectrale")); //bispectrale
        SubProduct couleurNaturelle = service.newSubProduct(new SubProduct("Couleur Naturelle", "natural")); //natural
        SubProduct brouillardNuagesBas = service.newSubProduct(new SubProduct("Brouillard Nuages Bas", "brouillard_nuagesbas")); //brd_nbas
        SubProduct convectionOrage = service.newSubProduct(new SubProduct("Convection Orage", "convstrm")); //convstrm

        service.addSubProductToProduct(pouvoirPrecipitant, satelliteDeveloppe);
        service.addSubProductToProduct(bispectrale, satelliteDeveloppe);
        service.addSubProductToProduct(couleurNaturelle, satelliteDeveloppe);
        service.addSubProductToProduct(brouillardNuagesBas, satelliteDeveloppe);
        service.addSubProductToProduct(convectionOrage, satelliteDeveloppe);

        SubProduct mosaicA = service.newSubProduct(new SubProduct("Mosaic A", "mosaic")); //mosaic
        SubProduct mosaicB = service.newSubProduct(new SubProduct("Mosaic B", "mosaicmosaic")); //mosaicmosaic
        SubProduct agadir250 = service.newSubProduct(new SubProduct("Agadir 250", "agadir250")); //agadir250
        SubProduct casa250 = service.newSubProduct(new SubProduct("Casa 250", "casa250")); //casa250
        SubProduct debdou250 = service.newSubProduct(new SubProduct("Debdou 250", "debdou250")); //debdou250
        SubProduct fes250 = service.newSubProduct(new SubProduct("Fes 250", "fes250")); //fes250

        service.addSubProductToProduct(mosaicA, radaStandard);
        service.addSubProductToProduct(mosaicB, radaStandard);
        service.addSubProductToProduct(agadir250, radaStandard);
        service.addSubProductToProduct(casa250, radaStandard);
        service.addSubProductToProduct(debdou250, radaStandard);
        service.addSubProductToProduct(fes250, radaStandard);

        SubProduct foudreMaroc = service.newSubProduct(new SubProduct("Foudre Maroc", "observation")); //foudre
        SubProduct lydec = service.newSubProduct(new SubProduct("Lydec", "lydec")); //foudre_ldc
        SubProduct jorfLasfer = service.newSubProduct(new SubProduct("Jorf Lasfer", "jorf_lasfer")); //jorf_lasfer
        SubProduct bassinBouregreg = service.newSubProduct(new SubProduct("Bassin Bouregreg", "bassin_versant")); //bouregreg
        SubProduct bassinLoukos = service.newSubProduct(new SubProduct("bassin Loukos", "bassin_versant")); //loukos
        SubProduct bassinOumErrabiae = service.newSubProduct(new SubProduct("bassin Oum Errabiae", "bassin_versant")); //oumerrabiae
        SubProduct bassinSebou = service.newSubProduct(new SubProduct("bassin Sebou", "bassin_versant")); //sebou
        SubProduct bassinTensift = service.newSubProduct(new SubProduct("bassin Tensiftx", "bassin_versant")); //tensift

        service.addSubProductToProduct(foudreMaroc, foudreStandard);
        service.addSubProductToProduct(lydec, foudreStandard);
        service.addSubProductToProduct(jorfLasfer, foudreStandard);
        service.addSubProductToProduct(bassinBouregreg, foudreStandard);
        service.addSubProductToProduct(bassinLoukos, foudreStandard);
        service.addSubProductToProduct(bassinOumErrabiae, foudreStandard);
        service.addSubProductToProduct(bassinSebou, foudreStandard);
        service.addSubProductToProduct(bassinTensift, foudreStandard);
    }
}
