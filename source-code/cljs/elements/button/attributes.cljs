
(ns elements.button.attributes
    (:require [dom.api          :as dom]
              [hiccup.api       :as hiccup]
              [pretty-css.api   :as pretty-css]
              [re-frame.api     :as r]
              [x.components.api :as x.components]))

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
  (-> {:class :e-button--icon}
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
  (-> {:class :e-button--label}
      (pretty-css/text-attributes button-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-body-attributes
  ; @ignore
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:disabled? (boolean)(opt)
  ;  :gap (keyword)(opt)
  ;  :horizontal-align (keyword)
  ;  :href (string)(opt)
  ;  :on-click (metamorphic-event)(opt)
  ;  :on-mouse-over (metamorphic-event)(opt)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-column-gap (keyword)
  ;  :data-selectable (boolean)
  ;  :disabled (boolean)
  ;  :href (string)
  ;  :id (string)
  ;  :on-click (function)
  ;  :on-mouse-over (function)
  ;  :on-mouse-up (function)
  ;  :style (map)}
  [button-id {:keys [disabled? gap horizontal-align href on-click on-mouse-over style] :as button-props}]
  ; XXX#4460
  ; By setting the :id attribute the body component becomes targetable for
  ; DOM actions. (setting focus/blur, etc.)
  (-> (if disabled? {:class                     :e-button--body
                     :disabled                  true
                     :data-letter-spacing       :auto
                     :data-column-gap           gap
                     :data-horizontal-row-align horizontal-align
                     :data-selectable           false
                     :style                     style}
                    {:class                     :e-button--body
                     :id                        (hiccup/value button-id "body")
                     :on-click                  #(r/dispatch  on-click)
                     :on-mouse-over             #(r/dispatch  on-mouse-over)
                     :on-mouse-up               #(dom/blur-active-element!)
                     :data-click-effect         :opacity
                     :data-letter-spacing       :auto
                     :data-column-gap           gap
                     :data-horizontal-row-align horizontal-align
                     :data-selectable           false
                     :style                     style})
      (pretty-css/badge-attributes    button-props)
      (pretty-css/border-attributes   button-props)
      (pretty-css/color-attributes    button-props)
      (pretty-css/font-attributes     button-props)
      (pretty-css/indent-attributes   button-props)
      (pretty-css/link-attributes     button-props)
      (pretty-css/progress-attributes button-props)
      (pretty-css/tooltip-attributes  button-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ button-props]
  (-> {:class :e-button}
      (pretty-css/default-attributes button-props)
      (pretty-css/outdent-attributes button-props)))
