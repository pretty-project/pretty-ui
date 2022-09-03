
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.combo-box.side-effects
    (:require [mid-fruits.vector                :as vector]
              [x.app-core.api                   :as a]
              [x.app-elements.combo-box.helpers :as combo-box.helpers]
              [x.app-elements.combo-box.state   :as combo-box.state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn highlight-next-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  (let [highlighted-option-dex (combo-box.helpers/get-highlighted-option-dex field-id)
        rendered-options       (combo-box.helpers/get-rendered-options       field-id field-props)]
       ; Ha nincs opció kiválasztva, akkor először az első opciót kell kijelölni ...
       (if (nil? highlighted-option-dex)
           (swap! combo-box.state/OPTION-HIGHLIGHTS assoc field-id 0)
           (let [next-option-dex (vector/next-dex rendered-options highlighted-option-dex)]
                (swap! combo-box.state/OPTION-HIGHLIGHTS assoc field-id next-option-dex)))))

(defn highlight-prev-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  (let [highlighted-option-dex (combo-box.helpers/get-highlighted-option-dex field-id)
        rendered-options       (combo-box.helpers/get-rendered-options       field-id field-props)
        prev-option-dex        (vector/prev-dex rendered-options highlighted-option-dex)]
       (swap! combo-box.state/OPTION-HIGHLIGHTS assoc field-id prev-option-dex)))

(defn discard-option-highlighter!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id _]
  (swap! combo-box.state/OPTION-HIGHLIGHTS dissoc field-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :elements.combo-box/highlight-next-option! highlight-next-option!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :elements.combo-box/highlight-prev-option! highlight-prev-option!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :elements.combo-box/discard-option-highlighter! discard-option-highlighter!)
