
(ns elements.button.helpers
    (:require [dom.api           :as dom]
              [hiccup.api        :as hiccup]
              [pretty-css.api    :as pretty-css]
              [re-frame.api      :as r]
              [x.environment.api :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-style-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:border-color (keyword or string)(opt)
  ;  :color (keyword or string)
  ;  :fill-color (keyword or string)(opt)
  ;  :hover-color (keyword)(opt)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [_ {:keys [border-color color fill-color hover-color style]}]
  (-> {:style style}
      (pretty-css/apply-color :border-color :data-border-color border-color)
      (pretty-css/apply-color :color        :data-color        color)
      (pretty-css/apply-color :fill-color   :data-fill-color   fill-color)
      (pretty-css/apply-color :hover-color  :data-hover-color  hover-color)))

(defn button-font-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:font-size (keyword)
  ;  :font-weight (keyword)
  ;  :line-height (keyword)}
  ;
  ; @return (map)
  ; {:data-font-size (keyword)
  ;  :data-font-weight (keyword)
  ;  :data-line-height (keyword)}
  [_ {:keys [font-size font-weight line-height]}]
  {:data-font-size   font-size
   :data-font-weight font-weight
   :data-line-height line-height})

(defn button-layout-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:border-radius (keyword)(opt)
  ;  :border-width (keyword)(opt)
  ;  :horizontal-align (keyword)}
  ;
  ; @return (map)
  ; {:data-border-radius (keyword)
  ;  :data-border-width (keyword)
  ;  :data-horizontal-row-align (keyword)}
  [_ {:keys [border-radius border-width horizontal-align]}]
  {:data-border-radius        border-radius
   :data-border-width         border-width
   :data-horizontal-row-align horizontal-align})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-icon-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:icon-color (keyword or string)
  ;  :icon-family (keyword)
  ;  :font-size (keyword)}
  ;
  ; @return (map)
  ; {:data-icon-family (keyword)
  ;  :data-icon-size (keyword)}
  [_ {:keys [icon-color icon-family icon-size]}]
  (-> {:data-icon-family icon-family
       :data-icon-size   icon-size}
      (pretty-css/apply-color :icon-color :data-icon-color icon-color)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:disabled? (boolean)(opt)
  ;  :on-click (metamorphic-event)(opt)
  ;  :on-mouse-over (metamorphic-event)(opt)}
  ;
  ; @return (map)
  ; {:data-click-effect (keyword)
  ;  :data-column-gap (keyword)
  ;  :data-selectable (boolean)
  ;  :disabled (boolean)
  ;  :id (string)
  ;  :on-click (function)
  ;  :on-mouse-over (function)
  ;  :on-mouse-up (function)}
  [button-id {:keys [disabled? on-click on-mouse-over] :as button-props}]
  ; XXX#4460
  ; By setting the :id attribute the body component becomes targetable for
  ; DOM actions. (setting focus/blur, etc.)
  (merge (pretty-css/indent-attributes       button-props)
         (pretty-css/badge-attributes        button-props)
         (button-style-attributes  button-id button-props)
         (button-font-attributes   button-id button-props)
         (button-layout-attributes button-id button-props)
         {:data-selectable false}
         (if disabled? {:disabled          true}
                       {:id                (hiccup/value button-id "body")
                        :on-click          #(r/dispatch  on-click)
                        :on-mouse-over     #(r/dispatch  on-mouse-over)
                        :on-mouse-up       #(x.environment/blur-element! button-id)
                        :data-click-effect :opacity})))

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
  (merge (pretty-css/default-attributes button-props)
         (pretty-css/outdent-attributes button-props)))
