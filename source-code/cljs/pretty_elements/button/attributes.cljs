
(ns pretty-elements.button.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-icon-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ button-props]
  (-> {:class :pe-button--icon}
      (pretty-attributes/icon-attributes button-props)))

(defn button-label-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ button-props]
  (-> {:class :pe-button--label}
      (pretty-attributes/font-attributes button-props)
      (pretty-attributes/text-attributes button-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-body-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ button-props]
  (-> {:class :pe-button--body}
      (pretty-attributes/anchor-attributes          button-props)
      (pretty-attributes/background-attributes      button-props)
      (pretty-attributes/badge-attributes           button-props)
      (pretty-attributes/border-attributes          button-props)
      (pretty-attributes/clickable-state-attributes button-props)
      (pretty-attributes/cursor-attributes          button-props)
      (pretty-attributes/flex-attributes            button-props)
      (pretty-attributes/full-block-size-attributes button-props)
      (pretty-attributes/effect-attributes          button-props)
      (pretty-attributes/marker-attributes          button-props)
      (pretty-attributes/focus-attributes           button-props)
      (pretty-attributes/mouse-event-attributes     button-props)
      (pretty-attributes/indent-attributes          button-props)
      (pretty-attributes/progress-attributes        button-props)
      (pretty-attributes/style-attributes           button-props)
      (pretty-attributes/tooltip-attributes         button-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ button-props]
  (-> {:class :pe-button}
      (pretty-attributes/class-attributes        button-props)
      (pretty-attributes/outdent-attributes      button-props)
      (pretty-attributes/state-attributes        button-props)
      (pretty-attributes/theme-attributes        button-props)
      (pretty-attributes/wrapper-size-attributes button-props)))
