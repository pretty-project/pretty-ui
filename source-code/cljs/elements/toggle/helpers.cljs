
(ns elements.toggle.helpers
    (:require [elements.button.helpers  :as button.helpers]
              [elements.element.helpers :as element.helpers]
              [hiccup.api               :as hiccup]
              [re-frame.api             :as r]
              [x.environment.api        :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-style-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ; {:border-color (keyword or string)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :hover-color (keyword or string)(opt)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [_ {:keys [border-color fill-color hover-color style]}]
  (-> {:style style}
      (element.helpers/apply-color :border-color :border-hover-color border-color)
      (element.helpers/apply-color :fill-color   :data-fill-color    fill-color)
      (element.helpers/apply-color :hover-color  :data-hover-color   hover-color)))

(defn toggle-layout-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) toggle-id
  ; @param (map) button-props
  ; {:border-radius (keyword)(opt)}
  ;
  ; @return (map)
  ; {:data-border-radius (keyword)}
  [_ {:keys [border-radius]}]
  {:data-border-radius border-radius})

(defn toggle-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ; {:disabled? (boolean)(opt)
  ;  :on-click (metamorphic-event)
  ;  :on-mouse-over (metamorphic-event)(opt)}
  ;
  ; @return (map)
  ; {:data-clickable (boolean)
  ;  :data-selectable (boolean)
  ;  :disabled (boolean)
  ;  :id (string)
  ;  :on-click (function)
  ;  :on-mouse-over (function)
  ;  :on-mouse-up (function)}
  [toggle-id {:keys [disabled? on-click on-mouse-over] :as toggle-props}]
  ; XXX#4460 (source-code/cljs/elements/button/helpers.cljs)
  (merge (element.helpers/element-indent-attributes toggle-id toggle-props)
         (toggle-style-attributes                   toggle-id toggle-props)
         (toggle-layout-attributes                  toggle-id toggle-props)
         {:data-selectable false}
         (if disabled? {:disabled       true}
                       {:id             (hiccup/value toggle-id "body")
                        :on-click       #(r/dispatch on-click)
                        :on-mouse-over  #(r/dispatch on-mouse-over)
                        :on-mouse-up    #(x.environment/blur-element! toggle-id)
                        :data-clickable true})))

(defn toggle-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ;
  ; @return (map)
  [toggle-id toggle-props]
  (merge (element.helpers/element-default-attributes toggle-id toggle-props)
         (element.helpers/element-outdent-attributes toggle-id toggle-props)))
