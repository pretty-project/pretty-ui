
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.toggle.helpers
    (:require [elements.button.helpers       :as button.helpers]
              [elements.element.helpers      :as element.helpers]
              [elements.element.side-effects :as element.side-effects]
              [hiccup.api                    :as hiccup]
              [re-frame.api                  :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-style-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ; {:background-color (keyword or string)(opt)
  ;  :hover-color (keyword)(opt)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [_ {:keys [background-color border-color color hover-color style]}]
  (-> {:style style}
      (element.helpers/apply-color :background-color :data-background-color background-color)
      (element.helpers/apply-color :hover-color      :data-hover-color           hover-color)))

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
  [toggle-id {:keys [disabled? on-mouse-over] :as toggle-props}]
  ; XXX#4460 (source-code/cljs/elements/button/helpers.cljs)
  (merge {:data-selectable false}
         (toggle-style-attributes  toggle-id toggle-props)
         (toggle-layout-attributes toggle-id toggle-props)
         (if disabled? {:disabled       true
                        ; XXX#0061 (source-code/cljs/elements/button/helpers.cljs)
                        :on-click       (button.helpers/on-click-f toggle-id toggle-props)}
                       {:id             (hiccup/value              toggle-id "body")
                        :on-click       (button.helpers/on-click-f toggle-id toggle-props)
                        :on-mouse-over  #(r/dispatch  on-mouse-over)
                        :on-mouse-up    #(element.side-effects/blur-element! toggle-id)
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
         (element.helpers/element-indent-attributes  toggle-id toggle-props)))
