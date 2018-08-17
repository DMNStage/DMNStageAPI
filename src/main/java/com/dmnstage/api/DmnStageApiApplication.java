package com.dmnstage.api;

import com.dmnstage.api.entities.*;
import com.dmnstage.api.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

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
        //service.setConfig(new Config("pathFormat", "api.dmnstage.com/img/#Product#-#Subproduct#-#Year#-#Month#-#Day#.jpg"));
        service.setConfig(new Config("pathFormat", "http://img.dmnstage.com/teledetection/#product#/#subProduct#/#year#-#month#-#day#/#hour##minute#.#ext#"));
        //service.setConfig(new Config("pathFormat", "extranet.marocmeteo.ma/samba/detections/#product#/#subProduct#/#month##day#/#subProduct#_#year##month##day##hour##minute#.#ext#"));

        Role adminRole = service.newRole(new Role("admin"));
        Role clientRole = service.newRole(new Role("client"));

        User admin1 = service.newUser(new Admin("AbdellahASKI", "654321", "Abdellah@aski.me", "+212707970909", 1, "Abdellah", "ASKI"));
        User admin2 = service.newUser(new Admin("kumohira", "654321", "youssef@naitsaid.me", "+21200000000", 1, "Youssef", "NAIT SAID"));

        service.addUserToRole(admin1, adminRole);
        service.addUserToRole(admin2, adminRole);

        //User U = service.getAdminById(1);
        //System.out.println(new BCryptPasswordEncoder().matches("654321", U.getPassword()));

        User client1 = service.newUser(new Client("client1", "654321", "client1@client1.com", "+21200000000", 1, "Client1"));
        User client2 = service.newUser(new Client("client2", "654321", "client2@client2.com", "+21200000000", 1, "Client2"));

        service.addUserToRole(client1, clientRole);
        service.addUserToRole(client2, clientRole);

        // Category teledetection = service.newCategory(new Category("Teledetection", "extranet.marocmeteo.ma/samba/detections/#Product#/#SUBProduct#/#Month##Day#/#SUBProduct#_#Year##Month##Day##Hour##Minute#.jpg"));
        //"http://extranet.marocmeteo.ma/samba/detections/"
        //"http://images.dmnstage.ccom/teledetection/#SUBProduct#/#Year##Month##Day#/#Hour##Minute#.jpg"

        Product satelliteStandard = service.newProduct(new Product("Satellite Standard", "sats"));
        Product satelliteDeveloppe = service.newProduct(new Product("Satellite Developpe", "rgbs"));
        Product radaStandard = service.newProduct(new Product("Radar Standard", "radars"));
        Product radarDeveloppe = service.newProduct(new Product("Radar Developpe", "srmats"));
        Product foudreStandard = service.newProduct(new Product("Foudre Standard", "foudres"));

        SubProduct canalIR = service.newSubProduct(new SubProduct("Canal IR", "ir", LocalTime.of(0, 0), LocalTime.of(23, 45), 15, "jpg")); //image name : nothing
        SubProduct canalVS = service.newSubProduct(new SubProduct("Canal VS", "vs", LocalTime.of(5, 0), LocalTime.of(17, 45), 15, "jpg")); //image name : nothing
        SubProduct canalWV = service.newSubProduct(new SubProduct("Canal WV", "wv", LocalTime.of(0, 0), LocalTime.of(23, 45), 15, "jpg")); //image name : nothing
        SubProduct canalIRNB = service.newSubProduct(new SubProduct("Canal IR NB", "nbir", LocalTime.of(0, 0), LocalTime.of(23, 45), 15, "jpg")); //image name : nothing
        SubProduct canalHRV = service.newSubProduct(new SubProduct("Canal HRV", "hrv", LocalTime.of(5, 0), LocalTime.of(17, 45), 15, "jpg")); //image name : nothing

        service.addSubProductToProduct(canalIR, satelliteStandard);
        service.addSubProductToProduct(canalVS, satelliteStandard);
        service.addSubProductToProduct(canalWV, satelliteStandard);
        service.addSubProductToProduct(canalIRNB, satelliteStandard);
        service.addSubProductToProduct(canalHRV, satelliteStandard);

        SubProduct pouvoirPrecipitant = service.newSubProduct(new SubProduct("Pouvoir Precipitant", "pouvoir_precipitant", LocalTime.of(0, 0), LocalTime.of(23, 45), 15, "jpg")); //pvr_prec
        SubProduct bispectrale = service.newSubProduct(new SubProduct("Bispectrale", "bispectrale", LocalTime.of(6, 0), LocalTime.of(18, 0), 15, "jpg")); //bispectrale
        SubProduct couleurNaturelle = service.newSubProduct(new SubProduct("Couleur Naturelle", "natural", LocalTime.of(6, 0), LocalTime.of(18, 0), 15, "jpg")); //natural
        SubProduct brouillardNuagesBas = service.newSubProduct(new SubProduct("Brouillard Nuages Bas", "brouillard_nuagesbas", LocalTime.of(0, 0), LocalTime.of(23, 45), 15, "jpg")); //brd_nbas
        SubProduct convectionOrage = service.newSubProduct(new SubProduct("Convection Orage", "convstrm", LocalTime.of(6, 0), LocalTime.of(18, 0), 15, "jpg")); //convstrm

        service.addSubProductToProduct(pouvoirPrecipitant, satelliteDeveloppe);
        service.addSubProductToProduct(bispectrale, satelliteDeveloppe);
        service.addSubProductToProduct(couleurNaturelle, satelliteDeveloppe);
        service.addSubProductToProduct(brouillardNuagesBas, satelliteDeveloppe);
        service.addSubProductToProduct(convectionOrage, satelliteDeveloppe);

        SubProduct mosaicA = service.newSubProduct(new SubProduct("Mosaic A", "mosaic", LocalTime.of(0, 0), LocalTime.of(23, 50), 10, "jpg")); //mosaic
        SubProduct mosaicB = service.newSubProduct(new SubProduct("Mosaic B", "mosaicmosaic", LocalTime.of(0, 0), LocalTime.of(23, 50), 10, "jpg")); //mosaicmosaic
        SubProduct agadir250 = service.newSubProduct(new SubProduct("Agadir 250", "agadir250", LocalTime.of(0, 0), LocalTime.of(23, 50), 10, "jpg")); //agadir250
        SubProduct casa250 = service.newSubProduct(new SubProduct("Casa 250", "casa250", LocalTime.of(0, 0), LocalTime.of(23, 50), 10, "jpg")); //casa250
        SubProduct debdou250 = service.newSubProduct(new SubProduct("Debdou 250", "debdou250", LocalTime.of(0, 0), LocalTime.of(23, 50), 10, "jpg")); //debdou250
        SubProduct fes250 = service.newSubProduct(new SubProduct("Fes 250", "fes250", LocalTime.of(0, 0), LocalTime.of(23, 50), 10, "jpg")); //fes250

        service.addSubProductToProduct(mosaicA, radaStandard);
        service.addSubProductToProduct(mosaicB, radaStandard);
        service.addSubProductToProduct(agadir250, radaStandard);
        service.addSubProductToProduct(casa250, radaStandard);
        service.addSubProductToProduct(debdou250, radaStandard);
        service.addSubProductToProduct(fes250, radaStandard);

        SubProduct foudreMaroc = service.newSubProduct(new SubProduct("Foudre Maroc", "observation", LocalTime.of(0, 0), LocalTime.of(23, 50), 10, "gif")); //foudre
        SubProduct lydec = service.newSubProduct(new SubProduct("Lydec", "lydec", LocalTime.of(0, 0), LocalTime.of(23, 50), 10, "gif")); //foudre_ldc
        SubProduct jorfLasfer = service.newSubProduct(new SubProduct("Jorf Lasfer", "jorf_lasfer", LocalTime.of(0, 0), LocalTime.of(23, 50), 10, "gif")); //jorf_lasfer
        SubProduct bassinBouregreg = service.newSubProduct(new SubProduct("Bassin Bouregreg", "bassin_versant", LocalTime.of(0, 0), LocalTime.of(23, 50), 10, "gif")); //bouregreg
        SubProduct bassinLoukos = service.newSubProduct(new SubProduct("bassin Loukos", "bassin_versant", LocalTime.of(0, 0), LocalTime.of(23, 50), 10, "gif")); //loukos
        SubProduct bassinOumErrabiae = service.newSubProduct(new SubProduct("bassin Oum Errabiae", "bassin_versant", LocalTime.of(0, 0), LocalTime.of(23, 50), 10, "gif")); //oumerrabiae
        SubProduct bassinSebou = service.newSubProduct(new SubProduct("bassin Sebou", "bassin_versant", LocalTime.of(0, 0), LocalTime.of(23, 50), 10, "gif")); //sebou
        SubProduct bassinTensift = service.newSubProduct(new SubProduct("bassin Tensiftx", "bassin_versant", LocalTime.of(0, 0), LocalTime.of(23, 50), 10, "gif")); //tensift

        service.addSubProductToProduct(foudreMaroc, foudreStandard);
        service.addSubProductToProduct(lydec, foudreStandard);
        service.addSubProductToProduct(jorfLasfer, foudreStandard);
        service.addSubProductToProduct(bassinBouregreg, foudreStandard);
        service.addSubProductToProduct(bassinLoukos, foudreStandard);
        service.addSubProductToProduct(bassinOumErrabiae, foudreStandard);
        service.addSubProductToProduct(bassinSebou, foudreStandard);
        service.addSubProductToProduct(bassinTensift, foudreStandard);

        service.mergeClientSubProduct((Client) client1, canalIR);
        service.mergeClientSubProduct((Client) client1, canalVS);
        service.mergeClientSubProduct((Client) client1, canalWV);
        service.mergeClientSubProduct((Client) client1, canalIRNB);
        service.mergeClientSubProduct((Client) client1, canalHRV);
        service.mergeClientSubProduct((Client) client1, pouvoirPrecipitant);
        service.mergeClientSubProduct((Client) client1, bispectrale);
        service.mergeClientSubProduct((Client) client1, couleurNaturelle);
        service.mergeClientSubProduct((Client) client1, brouillardNuagesBas);
        service.mergeClientSubProduct((Client) client1, convectionOrage);
        service.mergeClientSubProduct((Client) client2, mosaicA);
        service.mergeClientSubProduct((Client) client2, mosaicB);
        service.mergeClientSubProduct((Client) client2, agadir250);
        service.mergeClientSubProduct((Client) client2, casa250);
        service.mergeClientSubProduct((Client) client2, debdou250);
        service.mergeClientSubProduct((Client) client2, fes250);
        service.mergeClientSubProduct((Client) client2, foudreMaroc);
        service.mergeClientSubProduct((Client) client2, lydec);
        service.mergeClientSubProduct((Client) client2, jorfLasfer);
        service.mergeClientSubProduct((Client) client2, bassinBouregreg);
        service.mergeClientSubProduct((Client) client2, bassinLoukos);
        service.mergeClientSubProduct((Client) client2, bassinOumErrabiae);
        service.mergeClientSubProduct((Client) client2, bassinSebou);
        service.mergeClientSubProduct((Client) client2, bassinTensift);

    }
}
