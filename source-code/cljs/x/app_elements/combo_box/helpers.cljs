
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
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;  {}
  ;
  ; @return (function)
  [box-id {:keys [initial-options] :as box-props}]
  #(if initial-options (a/dispatch [:elements.combo-box/init-combo-box! box-id box-props])))



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
  ;  {}
  ; @param (*) option
  ;
  ; @return (boolean)
  [box-id {:keys [option-label-f] :as box-props} option]
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
