
(ns pretty-layouts.plain-surface.views
    (:require [fruits.random.api                       :as random]
              [metamorphic-content.api                 :as metamorphic-content]
              [pretty-engine.api                       :as pretty-engine]
              [pretty-layouts.plain-surface.attributes :as plain-surface.attributes]
              [pretty-layouts.plain-surface.prototypes :as plain-surface.prototypes]
              [pretty-presets.api                      :as pretty-presets]
              [re-frame.api                            :as r]
              [reagent.api                             :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- plain-surface
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ; {:content (metamorphic-content)(opt)}
  [surface-id {:keys [content] :as surface-props}]
  [:div {:class :pl-plain-surface}
        [:div (plain-surface.attributes/surface-body-attributes surface-id surface-props)
              [metamorphic-content/compose content]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- layout-lifecycles
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ; {}
  [surface-id {:keys [on-mount on-unmount] :as surface-props}]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch on-mount))
                       :component-will-unmount (fn [_ _] (r/dispatch on-unmount))
                       :reagent-render         (fn [_ surface-props] [plain-surface surface-id surface-props])}))

(defn layout
  ; @param (keyword)(opt) surface-id
  ; @param (map) surface-props
  ; {:content (metamorphic-content)
  ;  :content-orientation (keyword)(opt)
  ;   :horizontal, :vertical
  ;   Default: :vertical
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :on-mount (Re-Frame metamorphic-event)(opt)
  ;  :on-unmount (Re-Frame metamorphic-event)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)}
  ;
  ; @usage
  ; [plain-surface {...}]
  ;
  ; @usage
  ; [plain-surface :my-plain-surface {...}]
  ([surface-props]
   [layout (random/generate-keyword) surface-props])

  ([surface-id surface-props]
   ; @note (tutorials#parametering)
   (fn [_ surface-props]
       (let [surface-props (pretty-presets/apply-preset                      surface-props)
             surface-props (plain-surface.prototypes/surface-props-prototype surface-props)]
            [layout-lifecycles surface-id surface-props]))))
