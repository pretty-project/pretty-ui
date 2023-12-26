
(ns pretty-elements.color-selector.attributes
    (:require [dom.api           :as dom]
              [fruits.vector.api :as vector]
              [pretty-css.api    :as pretty-css]
              [re-frame.api      :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn color-selector-options-label-attributes
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {}
  [_ {{:keys [label]} :popup}]
  (if label {:class            :pe-color-selector--options--label
             :data-font-size   :s
             :data-font-weight :medium
             :data-line-height :text-block}
            {:class            :pe-color-selector--options--label
             :data-placeholder true}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn color-selector-option-attributes
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {:value-path (Re-Frame path vector)}
  ; @param (string) option
  ;
  ; @return (map)
  ; {}
  [selector-id {:keys [value-path] :as selector-props} option]
  (let [on-click [:pretty-elements.color-selector/toggle-option! selector-id selector-props option]
        selected-options @(r/subscribe [:get-item value-path])]
       {:class             :pe-color-selector--option
        :data-click-effect :opacity
        :data-hover-effect :opacity
        :data-icon-family  :material-symbols-outlined
        :data-collected    (vector/contains-item? selected-options option)
        :on-click          #(r/dispatch on-click)
        :on-mouse-up       #(dom/blur-active-element!)}))

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
  (-> {:class :pe-color-selector--options-body
       :style style}
      (pretty-css/indent-attributes selector-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn color-selector-attributes
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ selector-props]
  (-> {:class :pe-color-selector--options}
      (pretty-css/class-attributes   selector-props)
      (pretty-css/state-attributes   selector-props)
      (pretty-css/outdent-attributes selector-props)))
