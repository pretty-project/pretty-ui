
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.forms.helpers
    (:require [mid-fruits.mixed  :as mixed]
              [mid-fruits.regex  :refer [re-match?]]
              [mid-fruits.string :as string]
              [mid.forms.config  :as config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn valid-string
  ; WARNING! INCOMPLETE! DO NOT USE!
  ;
  ; @param (string) n
  ;
  ; @usage
  ;  (valid-string " abCd12 ")
  ;  =>
  ;  "abCd12"
  ;
  ; @return (string)
  [n]
  (string/trim n))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn pin?
  ; @param (string) n
  ;
  ; @usage
  ;  (pin? "0000")
  ;
  ; @return (boolean)
  [n]
  (string/length? n 4))

(defn password?
  ; @param (string) n
  ;
  ; @usage
  ;  (password? "Abcde1")
  ;
  ; @return (boolean)
  [n]
 ;(re-match? n PASSWORD-PATTERN))
  (string/nonempty? n))

(defn email-address?
  ; @param (string) n
  ;
  ; @usage
  ;  (email-address? "foo@bar.baz")
  ;
  ; @return (boolean)
  [n]
  (re-match? n config/EMAIL-PATTERN))

(defn phone-number?
  ; @param (string) n
  ;
  ; @usage
  ;  (phone-number? "+36301234567")
  ;
  ; @return (boolean)
  [n]
  (re-match? n config/PHONE-NUMBER-PATTERN))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn items-different?
  ; @param (map) a
  ; @param (map) b
  ; @param (vector) keys
  ;
  ; @usage
  ;  (items-different? {...} {...})
  ;
  ; @usage
  ;  (items-different? {...} {...} [...])
  ;
  ; @example
  ;  (items-different? {:color "Red"   :size "XL"}
  ;                    {:color "Green" :size "XL"})
  ;  =>
  ;  true
  ;
  ; @example
  ;  (items-different? {:color "Red"}
  ;                    {:color "Red" :size "XL"})
  ;  =>
  ;  false
  ;
  ; @example
  ;  (items-different? {:color "Red"   :size "XL"}
  ;                    {:color "Green" :size "XL"}
  ;                    [:color])
  ;  =>
  ;  true
  ;
  ; @example
  ;  (items-different? {:color "Red"   :size "XL"}
  ;                    {:color "Green" :size "XL"}
  ;                    [:size])
  ;  =>
  ;  false
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
