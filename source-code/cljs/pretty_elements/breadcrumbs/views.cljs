
(ns pretty-elements.breadcrumbs.views
    (:require [fruits.hiccup.api                      :as hiccup]
              [fruits.random.api                      :as random]
              [pretty-elements.breadcrumbs.attributes :as breadcrumbs.attributes]
              [pretty-elements.breadcrumbs.prototypes :as breadcrumbs.prototypes]
              [pretty-elements.crumb.views           :as crumb.views]
              [pretty-elements.engine.api :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api                            :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn breadcrumbs-crumb
  ; @ignore
  ;
  ; @param (integer) crumb-dex
  ; @param (map) crumb-props
  [crumb-dex crumb-props]
  (let [crumb-props (breadcrumbs.prototypes/crumb-props-prototype crumb-dex crumb-props)]
       [crumb.views/view crumb-props]))

(defn breadcrumbs
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; {:crumbs (maps in vector)(opt)}
  [breadcrumbs-id {:keys [crumbs] :as breadcrumbs-props}]
  [:div (breadcrumbs.attributes/breadcrumbs-attributes breadcrumbs-id breadcrumbs-props)
        [:div (breadcrumbs.attributes/breadcrumbs-body-attributes breadcrumbs-id breadcrumbs-props)
              (letfn [(f0 [crumb-dex crumb-props]
                          (if (-> crumb-dex zero?)
                              [:<> [breadcrumbs-crumb crumb-dex crumb-props]]
                              [:<> [:div (breadcrumbs.attributes/breadcrumbs-bullet-attributes breadcrumbs-id breadcrumbs-props)]
                                   [breadcrumbs-crumb crumb-dex crumb-props]]))]
                     (hiccup/put-with-indexed [:<>] crumbs f0))]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  [breadcrumbs-id breadcrumbs-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    breadcrumbs-id breadcrumbs-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount breadcrumbs-id breadcrumbs-props))
                       :reagent-render         (fn [_ breadcrumbs-props] [breadcrumbs breadcrumbs-id breadcrumbs-props])}))

(defn view
  ; @param (keyword)(opt) breadcrumbs-id
  ; @param (map) breadcrumbs-props
  ; {:bullet-color (keyword or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :crumb-default (map)(opt)
  ;  :crumbs (maps in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :gap (keyword, px or string)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :orientation (keyword)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :overflow (keyword)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [breadcrumbs {...}]
  ;
  ; @usage
  ; [breadcrumbs :my-breadcrumbs {...}]
  ([breadcrumbs-props]
   [view (random/generate-keyword) breadcrumbs-props])

  ([breadcrumbs-id breadcrumbs-props]
   ; @note (tutorials#parameterizing)
   (fn [_ breadcrumbs-props]
       (let [breadcrumbs-props (pretty-presets.engine/apply-preset                 breadcrumbs-id breadcrumbs-props)
             breadcrumbs-props (breadcrumbs.prototypes/breadcrumbs-props-prototype breadcrumbs-id breadcrumbs-props)
             breadcrumbs-props (pretty-elements.engine/apply-item-default          breadcrumbs-id breadcrumbs-props :crumbs :crumb-default)
             breadcrumbs-props (pretty-elements.engine/inherit-element-state       breadcrumbs-id breadcrumbs-props :crumbs :crumb-default)]
            [view-lifecycles breadcrumbs-id breadcrumbs-props]))))
