
(ns pretty-layouts.surface.views
    (:require [fruits.random.api                 :as random]
              [pretty-layouts.body.views         :as body.views]
              [pretty-layouts.engine.api         :as pretty-layouts.engine]
              [pretty-layouts.footer.views       :as footer.views]
              [pretty-layouts.header.views       :as header.views]
              [pretty-layouts.methods.api        :as pretty-layouts.methods]
              [pretty-layouts.surface.attributes :as surface.attributes]
              [pretty-layouts.surface.prototypes :as surface.prototypes]
              [pretty-subitems.api               :as pretty-subitems]
              [reagent.core                      :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- surface
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:body (map)(opt)
  ;  :footer (map)(opt)
  ;  :header (map)(opt)
  ;  ...}
  [id {:keys [body footer header] :as props}]
  [:div (surface.attributes/outer-attributes id props)
        [:div (surface.attributes/inner-attributes id props)
              (if header [header.views/view (pretty-subitems/subitem-id id :header) header])
              (if body   [:div (surface.attributes/content-attributes id props)
                               (if header [pretty-layouts.engine/layout-overlap-sensor (pretty-subitems/subitem-id id :header-sensor) header])
                               (if body   [body.views/view                             (pretty-subitems/subitem-id id :body)          body])
                               (if footer [pretty-layouts.engine/layout-overlap-sensor (pretty-subitems/subitem-id id :footer-sensor) footer])])
              (if footer [footer.views/view (pretty-subitems/subitem-id id :footer) footer])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-layouts.engine/layout-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-layouts.engine/layout-will-unmount id props))
                         :reagent-render         (fn [_ props] [surface id props])}))

(defn view
  ; @description
  ; Surface style layout.
  ;
  ; @links Implemented models
  ; [Container model](pretty-core/cljs/pretty-models/api.html#container-model)
  ; [Content model](pretty-core/cljs/pretty-models/api.html#content-model)
  ;
  ; @links Implemented properties
  ; [Fullscreen properties](pretty-core/cljs/pretty-properties/api.html#fullscreen-properties)
  ; [Keypress event properties](pretty-core/cljs/pretty-properties/api.html#keypress-event-properties)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented accessories.
  ; Check out the implemented layouts.
  ; Check out the implemented models.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-layouts/surface.png)
  ; [surface {:body       {:content "My body" :fill-color :highlight :outer-height :parent}
  ;           :header     {:content "My header"}
  ;           :footer     {:content "My footer"}
  ;           :fill-color :default}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-layouts.methods/apply-layout-shorthand-map  id props {:body :content :footer :content :header :content})
             props (pretty-layouts.methods/apply-layout-preset         id props)
             props (pretty-layouts.methods/import-layout-dynamic-props id props)
             props (pretty-layouts.methods/import-layout-state-events  id props)
             props (pretty-layouts.methods/import-layout-state         id props)
             props (surface.prototypes/props-prototype                 id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
