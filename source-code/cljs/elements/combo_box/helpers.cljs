
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.combo-box.helpers
    (:require [candy.api                   :refer [return]]
              [dom.api                     :as dom]
              [elements.combo-box.state    :as combo-box.state]
              [elements.input.helpers      :as input.helpers]
              [elements.text-field.helpers :as text-field.helpers]
              [hiccup.api                  :as hiccup]
              [re-frame.api                :as r]
              [string.api                  :as string]
              [vector.api                  :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn combo-box-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;  {:initial-options (vector)(opt)}
  [box-id {:keys [initial-options] :as box-props}]
  (if initial-options (r/dispatch [:elements.combo-box/combo-box-did-mount box-id box-props])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-highlighted-option-dex
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ;
  ; @return (integer)
  [box-id]
  (get @combo-box.state/OPTION-HIGHLIGHTS box-id))

(defn any-option-highlighted?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ;
  ; @return (boolean)
  [box-id]
  (let [highlighted-option-dex (get-highlighted-option-dex box-id)]
       (some? highlighted-option-dex)))

(defn render-option?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;  {:option-label-f (function)(opt)}
  ; @param (*) option
  ;
  ; @return (boolean)
  [box-id {:keys [option-label-f] :as box-props} option]
  ; XXX#51910
  (let [field-content (text-field.helpers/get-field-content box-id)
        option-label  (option-label-f option)]
       (and (string/not-pass-with? option-label field-content {:case-sensitive? false})
            (string/starts-with?   option-label field-content {:case-sensitive? false}))))

(defn get-rendered-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (vector)
  [box-id box-props]
  (let [options (input.helpers/get-input-options box-id box-props)]
       (letfn [(f [options option] (if (render-option? box-id box-props option)
                                       (conj   options option)
                                       (return options)))]
              (reduce f [] options))))

(defn get-highlighted-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (*)
  [box-id box-props]
  (if-let [highlighted-option-dex (get-highlighted-option-dex box-id)]
          (let [rendered-options (get-rendered-options box-id box-props)]
               (vector/nth-item rendered-options highlighted-option-dex))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn any-option-rendered?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (boolean)
  [box-id _]
  ; XXX#3270
  ;
  ; HACK#1450
  ; Amikor a text-field elem input mezője fókuszált állapotban van, akkor a mező alatt
  ; megjelenő surface felület minden esetben {:visible? true} állapotba lép,
  ; még akkor is, amikor az opciók listája nem tartalmazna elemet.
  ; A {:visible? true} állapotban lévő felület, ha nem jelenít meg sem választható
  ; opciót, sem pedig a options-placeholder feliratot (ki lett kapcsolva), akkor
  ; a felhasználó számára nem lenne látható, miközben az állapota mégis {:visible? true}.
  ; Ilyen esetben előfordulhatna, ha az [:elements.combo-box/ESC-pressed ...]
  ; esemény, mivel azt érzékelné, hogy a felület {:visible? true} állapotban van,
  ; ezért az ESC billentyű első lenyomására a felületet {:visible? false} állapotba
  ; állítaná, majd csak a második lenyomásra ürítené ki a mezőt, de mivel a felület
  ; a felhasználó számára nem lenne látható, ezért az ESC billentyű első lenyomása
  ; nem okozna látható változást és úgy tűnne, mintha a mező kiürítéséhez kettőször
  ; kellene megnyomni az ESC billentyűt.
  ;
  ; Lehetséges megoldások:
  ; 1. A surface felületen mindig megjelenne első (muted) elemként a mező tartalma.
  ;    (Nem jó, hogy ha abban az esetben is megjelenik a surface felület, amikor
  ;     nincsenek választható opciók és a beírt szöveget duplán látja a felhasználó!)
  ;
  ; 2. A surface felületen megjelenne egy options-placeholder felirat, amikor nincsenek
  ;    opciók kirenderelve.
  ;    (Nem jó, hogy ha csak abban az esetben is megjelenik a surface felület,
  ;     amikor nincsenek választható opciók!)
  ;
  ; 3. A combo-box a google.com kereső mezőjéhez hasonlóan működjön és az első
  ;    választható opció mindig a beírt érték legyen, a mező tartalma pedig előnézetben
  ;    mutassa a highlighted opció értékét, ami csak íródna a value-path útvonalra,
  ;    amikor ténylegesen ki lesz választva az adott opció.
  ;    (Nem lenne egyértelmű a felhasználó számára!)
  ;
  ; 4. Az [:elements.combo-box/ESC-pressed ...] esemény vizsgálná meg, hogy vannak
  ;    ténylegesen kirenderelve választható opciók.
  ;    (Tökéletes!)
  (boolean (let [surface-id (hiccup/value box-id "surface")]
                (if-let [surface-element (dom/get-element-by-id surface-id)]
                        (dom/get-element-by-query surface-element "[data-options-rendered=\"true\"]")))))
