
(ns pretty-elements.crumb.attributes
    (:require [pretty-css.appearance.api :as pretty-css.appearance]
              [pretty-css.accessories.api :as pretty-css.accessories]
              [pretty-css.basic.api      :as pretty-css.basic]
              [pretty-css.content.api    :as pretty-css.content]
              [pretty-css.control.api    :as pretty-css.control]
              [pretty-css.layout.api     :as pretty-css.layout]
              [pretty-css.live.api       :as pretty-css.live]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn crumb-label-attributes
  ; @ignore
  ;
  ; @param (keyword) crumb-id
  ; @param (map) crumb-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ crumb-props]
  (-> {:class :pe-crumb--label}
      (pretty-css.content/font-attributes              crumb-props)
      (pretty-css.content/unselectable-text-attributes crumb-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn crumb-body-attributes
  ; @ignore
  ;
  ; @param (keyword) crumb-id
  ; @param (map) crumb-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [crumb-id crumb-props]
  (-> {:class :pe-crumb--body}
      (pretty-css.basic/style-attributes            crumb-props)
      (pretty-css.control/anchor-attributes         crumb-props)
      (pretty-css.control/mouse-event-attributes    crumb-props)
      (pretty-css.control/state-attributes          crumb-props)
      (pretty-css.control/tab-attributes            crumb-props)
      (pretty-css.layout/flex-attributes            crumb-props)
      (pretty-css.layout/full-block-size-attributes crumb-props)
      (pretty-css.layout/indent-attributes          crumb-props)
      (pretty-css.live/effect-attributes            crumb-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn crumb-attributes
  ; @ignore
  ;
  ; @param (keyword) crumb-id
  ; @param (map) crumb-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ crumb-props]
  (-> {:class :pe-crumb}
      (pretty-css.appearance/theme-attributes    crumb-props)
      (pretty-css.layout/outdent-attributes      crumb-props)
      (pretty-css.basic/class-attributes         crumb-props)
      (pretty-css.basic/state-attributes         crumb-props)
      (pretty-css.layout/wrapper-size-attributes crumb-props)))
