
(ns elements.icon-button.attributes
    (:require [elements.button.attributes :as button.attributes]
              [pretty-css.api             :as pretty-css]
              [x.components.api           :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-icon-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ button-props]
  (-> {:class :e-icon-button--icon}
      (pretty-css/icon-attributes button-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-body-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [button-id button-props]
  (-> (button.attributes/button-body-attributes button-id button-props)
      (merge {:class :e-icon-button--body})
      (pretty-css/indent-attributes  button-props)
      (pretty-css/tooltip-attributes button-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ button-props]
  (-> {:class :e-icon-button}
      (pretty-css/default-attributes button-props)
      (pretty-css/outdent-attributes button-props)))
