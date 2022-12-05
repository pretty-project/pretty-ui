
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns iso.forms.helpers
    (:require [mixed.api  :as mixed]
              [string.api :as string]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn valid-string
  ; WARNING! INCOMPLETE! DO NOT USE!
  ;
  ; @param (string) n
  ;
  ; @usage
  ; (valid-string " abCd12 ")
  ; =>
  ; "abCd12"
  ;
  ; @return (string)
  [n]
  (string/trim n))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn items-different?
  ; @param (map) a
  ; @param (map) b
  ; @param (vector) keys
  ;
  ; @usage
  ; (items-different? {...} {...})
  ;
  ; @usage
  ; (items-different? {...} {...} [...])
  ;
  ; @example
  ; (items-different? {:color "Red"   :size "XL"}
  ;                   {:color "Green" :size "XL"})
  ; =>
  ; true
  ;
  ; @example
  ; (items-different? {:color "Red"}
  ;                   {:color "Red" :size "XL"})
  ; =>
  ; false
  ;
  ; @example
  ; (items-different? {:color "Red"   :size "XL"}
  ;                   {:color "Green" :size "XL"}
  ;                   [:color])
  ; =>
  ; true
  ;
  ; @example
  ; (items-different? {:color "Red"   :size "XL"}
  ;                   {:color "Green" :size "XL"}
  ;                   [:size])
  ; =>
  ; false
  ;
  ; @return (boolean)
  ([a b]
   ; 2.
   ; Ha a megvizsgálandó kulcsok nincsenek meghatározva, akkor az a térkép
   ; kulcsai szerint hasonlítja össze a és b térképet, tehát a térképet tekinti
   ; a megváltozott elemnek és b térképet az eredetiről készült másolatnak.
   (let [keys (keys a)]
        (items-different? a b keys)))

  ([a b keys]
   ; 1 .
   ; Összehasonlítja a és b térkép keys vektorban felsorolt kulcsú elemeit.
   ;
   ; XXX#6000
   ; A térkép megváltozottságának vizsgálatakor ...
   ; ... figyelembe veszi a különböző üres értékeket és egyformának tekinti őket!
   ;     Pl.: nil = "" = [] = ...
   ;     Pl.: Az egyes input mezők használatakor ha a felhasználó kiüríti a mezőt,
   ;          akkor a visszamaradó üres string értéket egyenlőnek kell tekintini
   ;          a mező használata előtti NIL értékkel!
   ;
   ; XXX#6001
   ; A térkép megváltozottságának vizsgálatakor ...
   ; ... ha a vizsgált és a tárolt érték string típusra alakítva megegyeznek,
   ;     akkor egyenlőnek kell tekintini őket!
   ;     Pl.: 42 = "42"
   ;     Pl.: A szövegmezőkben megadott számokat a szerver sok esetben integer
   ;          típusra alakítja!
   (letfn [(f [key] (if (-> a key mixed/blank?)
                        (-> b key mixed/nonempty?)
                        (not= (-> a key str)
                              (-> b key str))))]
          (some f keys))))
