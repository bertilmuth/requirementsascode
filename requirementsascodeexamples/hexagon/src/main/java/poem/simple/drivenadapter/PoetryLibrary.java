package poem.simple.drivenadapter;

import poem.hexagon.boundary.drivenport.IObtainPoems;

/**
 * Right-side, driven adapter that acts as repository for poems.
 * 
 * Inspired by a talk by A. Cockburn and T. Pierrain on hexagonal architecture:
 * https://www.youtube.com/watch?v=th4AgBcrEHA
 * 
 * @author b_muth
 *
 */
public class PoetryLibrary implements IObtainPoems {
	public String[] getMePoems(String language) {
		if ("de".equals(language)) {
			return new String[] {
					"DER PANTHER\nIM JARDIN DES PLANTES, PARIS\n\nSein Blick ist vom Vorübergehn der Stäbe\nso müd geworden, daß er nichts mehr hält.\nIhm ist, als ob es tausend Stäbe gäbe\nund hinter tausend Stäben keine Welt.\n\nDer weiche Gang geschmeidig starker Schritte,\nder sich im allerkleinsten Kreise dreht,\nist wie ein Tanz von Kraft um eine Mitte,\nin der betäubt ein großer Wille steht.\n\nNur manchmal schiebt der Vorhang der Pupille\nsich lautlos auf –. Dann geht ein Bild hinein,\ngeht durch der Glieder angespannte Stille –\nund hört im Herzen auf zu sein.\n\n\n--“Der Panther“ von Rainer Maria Rilke",
					"Ich sitze am Straßenrand\nDer Fahrer wechselt das Rad.\nIch bin nicht gern, wo ich herkomme.\nIch bin nicht gern, wo ich hinfahre.\nWarum sehe ich den Radwechsel\nMit Ungeduld?\n\n\n--“Der Radwechsel“ von Bertold Brecht" };

		} else {
			return new String[] {
					"I'm nobody! Who are you?\nAre you nobody, too?\nThen there's a pair of us -- don't tell!\nThey'd advertise -- you know!\n\nHow dreary to be somebody!\nHow public like a frog\nTo tell one's name the livelong day\nTo an admiring bog!\n\n--“Im nobody! Who are you?“ by Emily Dickinson",
					"Tyger! Tyger! burning bright\nIn the forests of the night,\nWhat immortal hand or eye,\nCould frame thy fearful symmetry?\n\nIn what distant deeps or skies\nBurnt the fire of thine eyes?\nOn what wings dare he aspire?\nWhat the hand dare seize the fire?\n\n-- “The Tyger” by William Blake",
					"From childhood's hour I have not been\nAs others were — I have not seen\nAs others saw — I could not bring\nMy passions from a common spring —\nFrom the same source I have not taken\nMy sorrow — I could not awaken\nMy heart to joy at the same tone —\nAnd all I lov'd — I lov'd alone —\nThen — in my childhood — in the dawn\nOf a most stormy life — was drawn\nFrom ev'ry depth of good and ill\nThe mystery which binds me still —\nFrom the torrent, or the fountain —\nFrom the red cliff of the mountain —\nFrom the sun that 'round me roll'd\nIn its autumn tint of gold —\nFrom the lightning in the sky\nAs it pass'd me flying by —\nFrom the thunder, and the storm —\nAnd the cloud that took the form\n(When the rest of Heaven was blue)\nOf a demon in my view — \n\n--“Alone“ by Edgar Allan Poe" };

		}
	}
}