
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.combo-box.side-effects
    (:require [mid-fruits.vector                 :as vector]
              [x.app-core.api                    :as a]
              [x.app-elements.combo-box.helpers  :as combo-box.helpers]
              [x.app-elements.combo-box.state    :as combo-box.state]
              [x.app-elements.text-field.helpers :as text-field.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn highlight-next-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  (let [highlighted-option-dex (combo-box.helpers/get-highlighted-option-dex box-id)
        rendered-options       (combo-box.helpers/get-rendered-options       box-id box-props)]
       ; Ha nincs opció kiválasztva, akkor először az első opciót kell kijelölni ...
       (if (nil? highlighted-option-dex)
           (swap! combo-box.state/OPTION-HIGHLIGHTS assoc box-id 0)
           (let [next-option-dex (vector/next-dex rendered-options highlighted-option-dex)]
                (swap! combo-box.state/OPTION-HIGHLIGHTS assoc box-id next-option-dex)))))

(defn highlight-prev-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  (let [highlighted-option-dex (combo-box.helpers/get-highlighted-option-dex box-id)
        rendered-options       (combo-box.helpers/get-rendered-options       box-id box-props)
        prev-option-dex        (vector/prev-dex rendered-options highlighted-option-dex)]
       (swap! combo-box.state/OPTION-HIGHLIGHTS assoc box-id prev-option-dex)))

(defn discard-option-highlighter!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id _]
  (swap! combo-box.state/OPTION-HIGHLIGHTS dissoc box-id))

(defn use-selected-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;  {}
  ; @param (*) selected-option
  [box-id {:keys [option-label-f] :as box-props} selected-option]
  (let [option-label (option-label-f selected-option)]
       (text-field.helpers/set-field-content! box-id box-props option-label)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :elements.combo-box/highlight-next-option! highlight-next-option!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :elements.combo-box/highlight-prev-option! highlight-prev-option!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :elements.combo-box/discard-option-highlighter! discard-option-highlighter!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :elements.combo-box/use-selected-option! use-selected-option!)