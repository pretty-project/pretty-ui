
(ns pretty-elements.button.attributes
    (:require [fruits.hiccup.api :as hiccup]
              [pretty-css.api    :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-icon-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ button-props]
  (-> {:class :pe-button--icon}
      (pretty-css/icon-attributes button-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-label-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ button-props]
  (-> {:class :pe-button--label}
      (pretty-css/text-attributes button-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-body-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:disabled? (boolean)(opt)
  ;  :gap (keyword, px or string)(opt)
  ;  :horizontal-align (keyword)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-column-gap (keyword)
  ;  :data-selectable (boolean)
  ;  :disabled (boolean)
  ;  :id (string)
  ;  :style (map)}
  [button-id {:keys [disabled? gap horizontal-align style] :as button-props}]
  ; @note (#4460)
  ; Setting the ':id' attribute on the button body component makes it targetable for DOM actions (e.g., focus, blur, etc.).
  (let [button-body-id (hiccup/value button-id "body")]
       (-> {:class                     :pe-button--body
            :data-column-gap           gap
            :data-letter-spacing       :auto
            :data-row-horizontal-align horizontal-align
            :data-selectable           false
            :disabled                  disabled?
            :id                        button-body-id
            :style                     style}
           (pretty-css/badge-attributes        button-props)
           (pretty-css/border-attributes       button-props)
           (pretty-css/color-attributes        button-props)
           (pretty-css/cursor-attributes       button-props)
           (pretty-css/effect-attributes       button-props)
           (pretty-css/element-size-attributes button-props)
           (pretty-css/font-attributes         button-props)
           (pretty-css/indent-attributes       button-props)
           (pretty-css/link-attributes         button-props)
           (pretty-css/marker-attributes       button-props)
           (pretty-css/mouse-event-attributes  button-props)
           (pretty-css/progress-attributes     button-props)
           (pretty-css/tooltip-attributes      button-props))))

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
  (-> {:class :pe-button}
      (pretty-css/class-attributes        button-props)
      (pretty-css/state-attributes        button-props)
      (pretty-css/outdent-attributes      button-props)
      (pretty-css/wrapper-size-attributes button-props)))
