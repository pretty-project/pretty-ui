
(ns elements.combo-box.side-effects
    (:require [elements.combo-box.env            :as combo-box.env]
              [elements.combo-box.state          :as combo-box.state]
              [elements.plain-field.side-effects :as plain-field.side-effects]
              [re-frame.api                      :as r]
              [vector.api                        :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn highlight-next-option!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  (let [highlighted-option-dex (combo-box.env/get-highlighted-option-dex box-id)
        rendered-options       (combo-box.env/get-rendered-options       box-id box-props)]
       ; If no option selected, then the first option has to be selected at the first time ...
       (if (nil? highlighted-option-dex)
           (swap! combo-box.state/OPTION-HIGHLIGHTS assoc box-id 0)
           (let [next-option-dex (vector/next-dex rendered-options highlighted-option-dex)]
                (swap! combo-box.state/OPTION-HIGHLIGHTS assoc box-id next-option-dex)))))

(defn highlight-prev-option!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  (let [highlighted-option-dex (combo-box.env/get-highlighted-option-dex box-id)
        rendered-options       (combo-box.env/get-rendered-options       box-id box-props)
        prev-option-dex        (vector/prev-dex rendered-options highlighted-option-dex)]
       (swap! combo-box.state/OPTION-HIGHLIGHTS assoc box-id prev-option-dex)))

(defn discard-option-highlighter!
  ; @ignore
  ;
  ; @param (keyword) box-id
  [box-id]
  (swap! combo-box.state/OPTION-HIGHLIGHTS dissoc box-id))

(defn use-selected-option!
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; {:option-label-f (function)}
  ; @param (*) selected-option
  [box-id {:keys [option-label-f] :as box-props} selected-option]
  (let [option-label (option-label-f selected-option)]
       (plain-field.side-effects/set-field-content! box-id option-label)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
(r/reg-fx :elements.combo-box/highlight-next-option! highlight-next-option!)

; @ignore
(r/reg-fx :elements.combo-box/highlight-prev-option! highlight-prev-option!)

; @ignore
(r/reg-fx :elements.combo-box/discard-option-highlighter! discard-option-highlighter!)

; @ignore
(r/reg-fx :elements.combo-box/use-selected-option! use-selected-option!)
