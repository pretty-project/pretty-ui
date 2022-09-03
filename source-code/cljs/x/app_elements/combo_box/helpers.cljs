
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.combo-box.helpers
    (:require [mid-fruits.candy                  :refer [return]]
              [mid-fruits.string                 :as string]
              [mid-fruits.vector                 :as vector]
              [x.app-core.api                    :as a]
              [x.app-elements.combo-box.state    :as combo-box.state]
              [x.app-elements.input.helpers      :as input.helpers]
              [x.app-elements.text-field.helpers :as text-field.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component-did-mount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  ;
  ; @return (function)
  [field-id {:keys [initial-options] :as field-props}]
  #(if initial-options (a/dispatch [:elements.combo-box/init-combo-box! field-id field-props])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-highlighted-option-dex
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (integer)
  [field-id]
  (get @combo-box.state/OPTION-HIGHLIGHTS field-id))

(defn any-option-highlighted?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (boolean)
  [field-id]
  (let [highlighted-option-dex (get-highlighted-option-dex field-id)]
       (some? highlighted-option-dex)))

(defn render-option?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  ; @param (*) option
  ;
  ; @return (boolean)
  [field-id {:keys [get-label-f] :as field-props} option]
  (let [field-value  (text-field.helpers/get-field-value field-id)
        option-label (get-label-f option)]
       (and (string/not-pass-with? option-label field-value {:case-sensitive? false})
            (string/starts-with?   option-label field-value {:case-sensitive? false}))))

(defn get-rendered-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (vector)
  [field-id field-props]
  (let [options (input.helpers/get-input-options field-id field-props)]
       (letfn [(f [options option] (if (render-option? field-id field-props option)
                                       (conj   options option)
                                       (return options)))]
              (reduce f [] options))))

(defn get-highlighted-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (*)
  [field-id field-props]
  (let [highlighted-option-dex (get-highlighted-option-dex field-id)
        rendered-options       (get-rendered-options       field-id field-props)]
       (vector/nth-item rendered-options highlighted-option-dex)))
