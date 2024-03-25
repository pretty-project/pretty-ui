
(ns pretty-layouts.sidebar.views
    (:require [fruits.random.api                 :as random]
              [pretty-accessories.overlay.views  :as overlay.views]
              [pretty-layouts.body.views         :as body.views]
              [pretty-layouts.engine.api         :as pretty-layouts.engine]
              [pretty-layouts.footer.views       :as footer.views]
              [pretty-layouts.header.views       :as header.views]
              [pretty-layouts.methods.api        :as pretty-layouts.methods]
              [pretty-layouts.sidebar.attributes :as sidebar.attributes]
              [pretty-layouts.sidebar.prototypes :as sidebar.prototypes]
              [pretty-subitems.api               :as pretty-subitems]
              [reagent.core                      :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-MAP {:body   body.views/SHORTHAND-KEY
                    :footer footer.views/SHORTHAND-KEY
                    :header header.views/SHORTHAND-KEY})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sidebar
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:body (map)(opt)
  ;  :footer (map)(opt)
  ;  :header (map)(opt)
  ;  :overlay (map)(opt)
  ;  ...}
  [id {:keys [body footer header overlay] :as props}]
  [:div (sidebar.attributes/outer-attributes id props)
        (if overlay [overlay.views/view (pretty-subitems/subitem-id id :overlay) overlay])
        [:div (sidebar.attributes/inner-attributes id props)
              (if header [header.views/view (pretty-subitems/subitem-id id :header) header])
              (if body   [:div (sidebar.attributes/scroll-container-attributes id props)
                               [pretty-layouts.engine/layout-header-sensor id props]
                               [body.views/view (pretty-subitems/subitem-id id :body) body]
                               [pretty-layouts.engine/layout-footer-sensor id props]])
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
                         :reagent-render         (fn [_ props] [sidebar id props])}))

(defn view
  ; @description
  ; Sidebar style layout.
  ;
  ; @todo
  ; Restore the sidebar layout's original functionality with controls and sidebar sensor.
  ;
  ; @links Implemented accessories
  ; [Overlay](pretty-ui/cljs/pretty-accessories/api.html#overlay)
  ;
  ; @links Implemented layouts
  ; [Body](pretty-ui/cljs/pretty-layouts/api.html#body)
  ; [Footer](pretty-ui/cljs/pretty-layouts/api.html#footer)
  ; [Header](pretty-ui/cljs/pretty-layouts/api.html#header)
  ;
  ; @links Implemented models
  ; [Flex container model](pretty-core/cljs/pretty-models/api.html#flex-container-model)
  ; [Plain content model](pretty-core/cljs/pretty-models/api.html#plain-content-model)
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
  ; @usage (pretty-layouts/sidebar.png)
  ; [sidebar {:body           {:content "My body" :fill-color :highlight :outer-height :parent}
  ;           :header         {:content "My header"}
  ;           :footer         {:content "My footer"}
  ;           :overlay        {:fill-color :inverse}
  ;           :fill-color     :default
  ;           :inner-position :left
  ;           :inner-width    :micro
  ;           :outer-width    :parent}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-layouts.methods/apply-layout-shorthand-map  id props SHORTHAND-MAP)
             props (pretty-layouts.methods/apply-layout-presets        id props)
             props (pretty-layouts.methods/import-layout-dynamic-props id props)
             props (pretty-layouts.methods/import-layout-state-events  id props)
             props (pretty-layouts.methods/import-layout-state         id props)
             props (sidebar.prototypes/props-prototype                 id props)]
            [view-lifecycles id props]))))
