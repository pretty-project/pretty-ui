
(ns pretty-elements.icon-button.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-elements.button.attributes :as button.attributes]
              [pretty-css.accessories.api :as pretty-css.accessories]
              [pretty-css.layout.api :as pretty-css.layout]))

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
  (-> {:class :pe-icon-button--icon}
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
      (merge {:class :pe-icon-button--body})
      (pretty-css.layout/indent-attributes  button-props)
      (pretty-css.accessories/tooltip-attributes button-props)))

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
  (-> {:class :pe-icon-button}
      (pretty-css/class-attributes   button-props)
      (pretty-css.layout/outdent-attributes button-props)
      (pretty-css/state-attributes   button-props)
      (pretty-css/theme-attributes   button-props)))
