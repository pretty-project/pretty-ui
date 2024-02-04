
(ns pretty-elements.icon-button.attributes
    (:require [pretty-css.accessories.api        :as pretty-css.accessories]
              [pretty-css.appearance.api         :as pretty-css.appearance]
              [pretty-css.basic.api              :as pretty-css.basic]
              [pretty-css.content.api            :as pretty-css.content]
              [pretty-css.layout.api             :as pretty-css.layout]
              [pretty-css.live.api       :as pretty-css.live]
              [pretty-elements.button.attributes :as button.attributes]
              [pretty-css.control.api    :as pretty-css.control]))

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
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [button-id button-props]
  (-> {:class :pe-icon-button--body}
      (pretty-css.accessories/badge-attributes      button-props)
      (pretty-css.accessories/marker-attributes     button-props)
      (pretty-css.accessories/tooltip-attributes    button-props)
      (pretty-css.appearance/background-attributes  button-props)
      (pretty-css.appearance/border-attributes      button-props)
      (pretty-css.basic/style-attributes            button-props)
      (pretty-css.control/anchor-attributes         button-props)
      (pretty-css.control/focus-attributes          button-props)
      (pretty-css.control/mouse-event-attributes    button-props)
      (pretty-css.control/state-attributes          button-props)
      (pretty-css.control/tab-attributes            button-props)
      (pretty-css.content/cursor-attributes         button-props)
      (pretty-css.layout/flex-attributes            button-props)
      (pretty-css.layout/full-block-size-attributes button-props)
      (pretty-css.layout/indent-attributes          button-props)
      (pretty-css.live/effect-attributes            button-props)   ; <- ez a button-tol jött, kell ide?
      (pretty-css.live/progress-attributes          button-props)))

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
  (-> {:class :pe-icon-button}
      (pretty-css.basic/class-attributes   button-props)
      (pretty-css.layout/outdent-attributes button-props)
      (pretty-css.basic/state-attributes   button-props)
      (pretty-css.appearance/theme-attributes   button-props)))
