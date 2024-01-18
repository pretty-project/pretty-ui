
(ns pretty-inputs.input.env
    (:require [fruits.vector.api         :as vector]
              [pretty-inputs.input.state :as input.state]
              [re-frame.api              :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-focused?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [input-id]
  (-> @input.state/FOCUSED-INPUTS input-id))

(defn input-visited?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [input-id]
  (-> @input.state/VISITED-INPUTS input-id))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-rendered?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [input-id]
  (= input-id @input.state/RENDERED-POPUP))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-input-value
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:projected-value (*)(opt)
  ;  :value (*)(opt)
  ;  :value-path (Re-Frame path vector)(opt)}
  ;
  ; @return (*)
  [input-id {:keys [projected-value value value-path]}]
  (or value @(r/subscribe [:get-item value-path])
            (if-not (input-visited? input-id) projected-value)))

(defn get-input-options
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:options (vector)(opt)
  ;  :options-path (Re-Frame path vector)(opt)}
  ;
  ; @return (vector)
  [_ {:keys [options options-path]}]
  (or options @(r/subscribe [:get-item options-path])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn option-checked?
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:option-value-f (function)}
  ; @param (*) option
  ;
  ; @return (boolean)
  [input-id {:keys [option-value-f] :as input-props} option]
  (let [input-options (get-input-options input-id input-props)
        input-value   (get-input-value   input-id input-props)
        option-value  (option-value-f    option)]
       (if (vector/count-min? input-options 2)
           (vector/contains-item? input-value option-value)
           (=                     input-value option-value))))
