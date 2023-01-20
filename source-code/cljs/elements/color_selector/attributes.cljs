
(ns elements.color-selector.attributes
    (:require [pretty-css.api    :as pretty-css]
              [re-frame.api      :as r]
              [x.environment.api :as x.environment]
              [vector.api        :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn color-selector-option-attributes
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {:value-path (vector)}
  ; @param (string) option
  ;
  ; @return (map)
  ; {}
  [selector-id {:keys [value-path] :as selector-props} option]
  (let [on-click [:elements.color-selector/toggle-option! selector-id selector-props option]
        selected-options @(r/subscribe [:x.db/get-item value-path])]
       {:class             :e-color-selector--option
        :data-click-effect :opacity
        :data-icon-family  :material-symbols-outlined
        :data-collected    (vector/contains-item? selected-options option)
        :on-click          #(r/dispatch on-click)
        :on-mouse-up       #(x.environment/blur-element! selector-id)}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn color-selector-body-attributes
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [style] :as selector-props}]
  (-> {:class :e-color-selector--options-body
       :style style}
      (pretty-css/indent-attributes selector-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn color-selector-attributes
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {:size (keyword)}
  ;
  ; @return (map)
  ; {:data-size (keyword)}
  [_ {:keys [size] :as selector-props}]
  (-> {:data-size size}
      (pretty-css/default-attributes selector-props)
      (pretty-css/outdent-attributes selector-props)))
