
(ns pretty-elements.icon-button.attributes
    (:require [pretty-elements.button.attributes :as button.attributes]
              [pretty-css.accessories.api :as pretty-css.accessories]
              [pretty-css.content.api :as pretty-css.content]
              [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.basic.api :as pretty-css.basic]
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
      (pretty-css.content/icon-attributes button-props)))

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
      (pretty-css.basic/class-attributes   button-props)
      (pretty-css.layout/outdent-attributes button-props)
      (pretty-css.basic/state-attributes   button-props)
      (pretty-css.appearance/theme-attributes   button-props)))
