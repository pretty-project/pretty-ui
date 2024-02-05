
(ns pretty-elements.surface.views
    (:require [fruits.random.api                          :as random]
              [pretty-elements.surface.prototypes :as surface.prototypes]
              [pretty-elements.surface.attributes :as surface.attributes]
              [pretty-elements.surface.config      :as surface.config]
              [pretty-elements.engine.api :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api :as reagent]
              [transition-controller.api :as transition-controller]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface-controller
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ; {:content (metamorphic-content)(opt)}
  [surface-id {:keys [content]}]
  ; If ...
  ; ... the 'view' component is mounted,
  ; ... the surface visibility is turned off,
  ; ... the initial content is provided,
  ; and the 'set-surface-content!' function turns on the visibility of the surface then sets a new content;
  ; the re-mounting transition controller unavoidably overrides the just stored new content with the provided
  ; initial content (set by the controller's component-did-mount lifecycle).
  ;
  ;
  [transition-controller/view surface-id {:initial-content     (if content [:div {:class :pe-surface--content} content])
                                          :transition-duration (-> surface.config/TRANSITION-DURATION)}])

(defn- surface
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props

  [surface-id surface-props]
 [:div (str "visible?" (pretty-elements.engine/element-visible? surface-id surface-props))
  (if (pretty-elements.engine/element-visible? surface-id surface-props)
      [:div (surface.attributes/surface-attributes surface-id surface-props)
            [:div "visible"]
            [:div (surface.attributes/surface-body-attributes surface-id surface-props)
                  [surface-controller                         surface-id surface-props]]])])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  [surface-id surface-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    surface-id surface-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount surface-id surface-props))
                       :component-did-update   (fn [%]   (pretty-elements.engine/element-did-update   surface-id surface-props %))
                       :reagent-render         (fn [_ surface-props] [surface surface-id surface-props])}))

(defn view
  ; @description
  ; Surface element for displaying interchangeable content with optionally animated transitions,
  ; and additional controller functions.
  ;
  ; @param (keyword)(opt) surface-id
  ; @param (map) surface-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-position (keyword)(opt)
  ;  :border-radius (map)(opt)
  ;   {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;  :border-width (keyword, px or string)(opt)
  ;  :class (keyword or keywords in vector)(opt)
  ;  :content (metamorphic-content)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;  :height (keyword, px or string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :layer (keyword or integer)(opt)
  ;  :max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)
  ;  :min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :positioning (keyword)(opt)
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;  :visible? (boolean)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [surface {...}]
  ;
  ; @usage
  ; [surface :my-surface {...}]
  ([surface-props]
   [view (random/generate-keyword) surface-props])

  ([surface-id surface-props]
   ; @note (tutorials#parameterizing)
   (fn [_ surface-props]
       (let [surface-props (pretty-presets.engine/apply-preset                  surface-id surface-props)
             surface-props (surface.prototypes/surface-props-prototype          surface-id surface-props)
             surface-props (pretty-elements.engine/import-element-dynamic-props surface-id surface-props)]
          [:div (str surface-props)
            [view-lifecycles surface-id surface-props]]))))


; kell hogy visible? false-el lehessen inditani
; kell hogy reagaljan arra ha megvaltozik a visible property
; de az is kell hogy lehessen fuggvénnyel ki-be kapcsolni a visibilit
; kell a dynamic props is
; de ha visible false-al indul
; akkor a set-content fgv bekapcsolja a visibilit, és beállít egy dynamic props-ot (animation-direction)
; emiatt az did update, újra kikapcsolja a visibilit mert azt látja a props-ban, hogy still off

; a másik, hogy mi van akkor ha beállitok fgv-el egy content-et majd kikapcsolom a visibilit
; majd visszakapcsolom és akkor megint az initial content et beállítja a content-nek a transition controller
; van benne



; talán segit
; két külön element: content-swapper (nem lehet ki-be kapcsolni), content-surface (nem lehet kontentet váltani)
; vagy akár: {:initial-visibility (boolean) :initial-content (metamorphic-content)}
;
