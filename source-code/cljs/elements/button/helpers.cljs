
(ns elements.button.helpers
    (:require [dom.api           :as dom]
              [hiccup.api        :as hiccup]
              [pretty-css.api    :as pretty-css]
              [re-frame.api      :as r]
              [x.components.api  :as x.components]
              [x.environment.api :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:disabled? (boolean)(opt)
  ;  :on-click (metamorphic-event)(opt)
  ;  :on-mouse-over (metamorphic-event)(opt)
  ;  :style (map)(opt)
  ;  :tooltip (metamorphic-content)(opt)
  ;  :tooltip-position (keyword)(opt)}
  ;
  ; @return (map)
  ; {:data-click-effect (keyword)
  ;  :data-column-gap (keyword)
  ;  :data-bubble-content (string)
  ;  :data-bubble-position (keyword)
  ;  :data-selectable (boolean)
  ;  :disabled (boolean)
  ;  :id (string)
  ;  :on-click (function)
  ;  :on-mouse-over (function)
  ;  :on-mouse-up (function)
  ;  :style (map)}
  [button-id {:keys [disabled? horizontal-align on-click on-mouse-over style tooltip tooltip-position]
              :as button-props}]
  ; XXX#4460
  ; By setting the :id attribute the body component becomes targetable for
  ; DOM actions. (setting focus/blur, etc.)
  (-> (if disabled? {:disabled                  true
                     :data-horizontal-row-align horizontal-align
                     :data-selectable           false
                     :style                     style}
                    {:id                        (hiccup/value button-id "body")
                     :on-click                  #(r/dispatch  on-click)
                     :on-mouse-over             #(r/dispatch  on-mouse-over)
                     :on-mouse-up               #(x.environment/blur-element! button-id)
                     :data-click-effect         :opacity
                     :data-bubble-content       (x.components/content tooltip)
                     :data-bubble-position      tooltip-position
                     :data-horizontal-row-align horizontal-align
                     :data-selectable           false
                     :style                     style})
      (pretty-css/badge-attributes  button-props)
      (pretty-css/border-attributes button-props)
      (pretty-css/color-attributes  button-props)
      (pretty-css/font-attributes   button-props)
      (pretty-css/indent-attributes button-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  [_ button-props]
  (-> {} (pretty-css/default-attributes button-props)
         (pretty-css/outdent-attributes button-props)))
