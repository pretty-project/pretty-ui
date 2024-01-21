
(ns pretty-inputs.color-selector.attributes
    (:require [dom.api              :as dom]
              [fruits.vector.api    :as vector]
              [pretty-build-kit.api :as pretty-build-kit]
              [re-frame.api         :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn color-selector-options-label-attributes
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {}
  [_ {{:keys [label]} :popup}]
  (if label {:class            :pi-color-selector--options--label
             :data-font-size   :s
             :data-font-weight :medium
             :data-line-height :text-block}
            {:class            :pi-color-selector--options--label
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
  (let [on-click [:pretty-inputs.color-selector/toggle-option! selector-id selector-props option]
        selected-options @(r/subscribe [:get-item value-path])]
       {:class             :pi-color-selector--option
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
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ selector-props]
  (-> {:class :pi-color-selector--options-body}
      (pretty-build-kit/indent-attributes selector-props)
      (pretty-build-kit/style-attributes  selector-props)))

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
  (-> {:class :pi-color-selector--options}
      (pretty-build-kit/class-attributes   selector-props)
      (pretty-build-kit/outdent-attributes selector-props)
      (pretty-build-kit/state-attributes   selector-props)))
