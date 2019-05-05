package hexagon.adapter;

import hexagon.port.IObtainPoems;

public class GermanPoetryLibrary implements IObtainPoems {
	public String[] getMePoems() {
		return new String[] {
				"DER PANTHER\nIM JARDIN DES PLANTES, PARIS\n\nSein Blick ist vom Vorübergehn der Stäbe\nso müd geworden, daß er nichts mehr hält.\nIhm ist, als ob es tausend Stäbe gäbe\nund hinter tausend Stäben keine Welt.\n\nDer weiche Gang geschmeidig starker Schritte,\nder sich im allerkleinsten Kreise dreht,\nist wie ein Tanz von Kraft um eine Mitte,\nin der betäubt ein großer Wille steht.\n\nNur manchmal schiebt der Vorhang der Pupille\nsich lautlos auf –. Dann geht ein Bild hinein,\ngeht durch der Glieder angespannte Stille –\nund hört im Herzen auf zu sein.\n\n\n--“Der Panther“ von Rainer Maria Rilke",
				"Ich sitze am Straßenrand\nDer Fahrer wechselt das Rad.\nIch bin nicht gern, wo ich herkomme.\nIch bin nicht gern, wo ich hinfahre.\nWarum sehe ich den Radwechsel\nMit Ungeduld?\n\n\n--“Der Radwechsel“ von Bertold Brecht" };
	}
}