*** NAZEV ***

Aplikace pro navrh a editaci blokových schémat.

*** CLENOVE TYMU ***

Danil Grigorev   - xgrigo02
Viki Cervenanska - xcerve23

*** PREHLED ***

Tento projekt implementuje blokovou kalkulacku, schopnou provadet operaci scitani, odcitani, deleni, nasobeni.
Pro tento ucel se pouzivaji bloky, vypocet je mozne krokovat. Vstupy se davaji na IN bloky, vystup se dá uvidet na OUT
blocich. Dokud schema neni spusteno, nedá se zadát hodnoty na IN bloky, ale je mozne schema libovolně upravovat.

*** ROZHRANI SHEMATU ***

Kazdy blok ma na leve strane vstupni porty, na prave vystupni. Vstupni port se da propit jenom s vystupnim, a naopak.
Propoj se nevytvori kdyz vznikne cyklus.

Pro vytvoreni propoje, natlacte na port mysi, kdyz schema neni spustena. Ten se zobrazi cervene. Potom zvolte jiny port,
pokud podmínky vytvareni propoje jsou splneny, tak se zobrazi cara.

Po zmacknuti leveho tlacitka mysi na care, se vytvori kloub, ktery se da libovolne posouvat.

Pri navedeni mysi na kterykoliv element schematu, se po nejake dobe zobrazi informace o stavu, vcetne obsazene
hodnoty.

Pro zadani hodnoty na block IN, musite spustit schema, natlacit na pozadovany blok a zadat typ a hodnotu v okne.


*** APPLIKACNI ROZHRANI ***

Horni menu obsahuje volby pro zapis a nacteni schematu do souboru.

V levem menu je mozne zvolit blok, ktery potom pridate na schema natlacenim leveho tlacika mysi.

Pro operaci mazani, natlacte na "Remove". Dokud remove neni natlaceno znovu, nebo nebyla zvolena jina volba menu ci
zmacknuto "Esc", mazani pokracuje.

*** IMPLEMENTACE ***

Pro implementaci rozhrani byla pouzita knihovna javafx.

Uchovani a nacitani shcematu je udelano pomoci serializaci informaci o objektech do/ze souboru  "*.scm".



*** OMEZENI ***

Bohuzel v implementaci projektu chybi moznost zvetsovani nebo zmensovani pracovni plochy pro umisteni bloku, a tak je
omezena soucasnou velikosti okna.

Take se nepovedlo naimplementovat operaci undo, a pridani vlastnich bloku.