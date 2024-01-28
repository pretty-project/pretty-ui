
(ns pretty-elements.breadcrumbs.views
    (:require [fruits.hiccup.api                      :as hiccup]
              [fruits.random.api                      :as random]
              [pretty-elements.breadcrumbs.attributes :as breadcrumbs.attributes]
              [pretty-elements.breadcrumbs.prototypes :as breadcrumbs.prototypes]
              [pretty-elements.button.views           :as button.views]
              [pretty-engine.api                      :as pretty-engine]
              [pretty-presets.api                     :as pretty-presets]
              [reagent.api                            :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn breadcrumbs
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; {:crumbs (maps in vector)(opt)}
  [breadcrumbs-id {:keys [crumbs] :as breadcrumbs-props}]
  [:div (breadcrumbs.attributes/breadcrumbs-attributes breadcrumbs-id breadcrumbs-props)
        [:div (breadcrumbs.attributes/breadcrumbs-body-attributes breadcrumbs-id breadcrumbs-props)
              (letfn [(f0 [crumb-dex crumb-props] [:<> (if  (-> crumb-dex pos?) [:div {:class :pe-breadcrumbs--separator}])
                                                       (let [crumb-props (breadcrumbs.prototypes/crumb-props-prototype crumb-props)]
                                                            [button.views/element crumb-props])])]
                     (hiccup/put-with-indexed [:<>] crumbs f0))]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- element-lifecycles
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  [breadcrumbs-id breadcrumbs-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-engine/element-did-mount    breadcrumbs-id breadcrumbs-props))
                       :component-will-unmount (fn [_ _] (pretty-engine/element-will-unmount breadcrumbs-id breadcrumbs-props))
                       :reagent-render         (fn [_ breadcrumbs-props] [breadcrumbs breadcrumbs-id breadcrumbs-props])}))

(defn element
  ; @note
  ; Crumbs take the same properties as buttons.
  ;
  ; @param (keyword)(opt) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :crumbs (maps in vector)
  ;  :disabled? (boolean)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)}
  ;
  ; @preview (pretty-elements/breadcrumbs.png)
  ;
  ; @usage
  ; [breadcrumbs {...}]
  ;
  ; @usage
  ; [breadcrumbs :my-breadcrumbs {...}]
  ([breadcrumbs-props]
   [element (random/generate-keyword) breadcrumbs-props])

  ([breadcrumbs-id breadcrumbs-props]
   ; @note (tutorials#parametering)
   (fn [_ breadcrumbs-props]
       (let [breadcrumbs-props (pretty-presets/apply-preset                        breadcrumbs-id breadcrumbs-props)
             breadcrumbs-props (breadcrumbs.prototypes/breadcrumbs-props-prototype breadcrumbs-id breadcrumbs-props)]
            [element-lifecycles breadcrumbs-id breadcrumbs-props]))))
