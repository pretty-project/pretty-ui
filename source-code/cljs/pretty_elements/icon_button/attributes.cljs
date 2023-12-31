
(ns pretty-elements.icon-button.attributes
    (:require [pretty-build-kit.api                    :as pretty-build-kit]
              [pretty-elements.button.attributes :as button.attributes]))

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
      (pretty-build-kit/icon-attributes button-props)))

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
      (pretty-build-kit/indent-attributes  button-props)
      (pretty-build-kit/tooltip-attributes button-props)))

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
      (pretty-build-kit/class-attributes   button-props)
      (pretty-build-kit/outdent-attributes button-props)
      (pretty-build-kit/state-attributes   button-props)))
