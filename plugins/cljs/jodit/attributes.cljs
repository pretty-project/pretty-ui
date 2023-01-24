
(ns jodit.attributes
    (:require [jodit.helpers :as helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn jodit-attributes
  ; @ignore
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ; {}
  ;
  ; @return (map)
  ; {:config (map)
  ;  :on-blur (function)
  ;  :on-change (function)
  ;  :on-focus (function)
  ;  :key (string)
  ;  :tabIndex (integer)
  ;  :value (string)}
  [editor-id {:keys [on-blur on-change on-focus update-trigger value] :as editor-props}]
  ; A key paraméter megváltozásának hatására a szerkesztő tartalma a value paraméter aktuális
  ; értéke lesz.
  ; Pl.: Ha a value paraméter a szerkesztő React-fába csatolásakor megkapja a szerkesztő kezdeti
  ;      tartalmát és később a felhasználó megnyomja a "Visszaállítás" gombot, hogy a szerkesztő
  ;      tartalma újra a megnyitáskori állapot szerinti legyen, akkor szükséges megváltoztatni
  ;      a key paraméter értékét, mivel a visszaállítás után a value paraméter "új" értéke
  ;      megegyezik az utoljára a megnyitáskor átadott értékével ezért a szerkesztő
  ;      component-did-update életciklusa nem triggerelődne.
  ;
  ; A key paraméter esetleges változtatása a szerkesztő villanását okozza.
  ;
  ; TODO
  ; Az on-change függvény számára a mező értékét is szükséges átadni!
  {:config     (helpers/jodit-config editor-props)
   :on-blur    (fn [_] (if on-blur   (on-blur   editor-id editor-props)))
   :on-focus   (fn [_] (if on-focus  (on-focus  editor-id editor-props)))
   :on-change  (fn [_] (if on-change (on-change editor-id editor-props)))
   :key        update-trigger
   :value      value
   :tabIndex   1})
