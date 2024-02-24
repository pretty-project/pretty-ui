
(ns pretty-layouts.sidebar.views
    (:require [fruits.random.api                 :as random]
              [pretty-layouts.engine.api         :as pretty-layouts.engine]
              [pretty-subitems.api         :as pretty-subitems]
              [pretty-layouts.sidebar.attributes :as sidebar.attributes]
              [pretty-layouts.sidebar.prototypes :as sidebar.prototypes]
              [pretty-presets.engine.api         :as pretty-presets.engine]
              [reagent.core :as reagent]
              [pretty-layouts.footer.views :as footer.views]
              [pretty-layouts.body.views :as body.views]
              [pretty-layouts.header.views :as header.views]
              [pretty-accessories.api :as pretty-accessories]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sidebar-header
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  [sidebar-id sidebar-props]
  (let [header-id    (pretty-subitems/subitem-id                sidebar-id :header)
        header-props (sidebar.prototypes/header-props-prototype sidebar-id sidebar-props)]
       [header.views/view header-id header-props]))

(defn- sidebar-body
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ; {:footer (map)(opt)
  ;  :header (map)(opt)
  ;  ...}
  [sidebar-id {:keys [footer header] :as sidebar-props}]
  (let [body-id    (pretty-subitems/subitem-id              sidebar-id :body)
        body-props (sidebar.prototypes/body-props-prototype sidebar-id sidebar-props)]
       [:div (sidebar.attributes/sidebar-content-attributes sidebar-id sidebar-props)
             (when header  [pretty-layouts.engine/layout-header-sensor sidebar-id sidebar-props])
             (when :always [body.views/view                            body-id    body-props])
             (when footer  [pretty-layouts.engine/layout-footer-sensor sidebar-id sidebar-props])]))

(defn- sidebar-footer
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  [sidebar-id sidebar-props]
  (let [footer-id    (pretty-subitems/subitem-id                sidebar-id :footer)
        footer-props (sidebar.prototypes/footer-props-prototype sidebar-id sidebar-props)]
       [footer.views/view footer-id footer-props]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sidebar
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  ; {:body (map)(opt)
  ;  :footer (map)(opt)
  ;  :header (map)(opt)
  ;  :overlay (map)(opt)
  ;  ...}
  [sidebar-id {:keys [body footer header overlay] :as sidebar-props}]
  [:div (sidebar.attributes/sidebar-attributes sidebar-id sidebar-props)
        (if overlay [pretty-accessories/overlay sidebar-id overlay])
        [:div (sidebar.attributes/sidebar-inner-attributes sidebar-id sidebar-props)
              (if header [sidebar-header sidebar-id sidebar-props])
              (if body   [sidebar-body   sidebar-id sidebar-props])
              (if footer [sidebar-footer sidebar-id sidebar-props])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) sidebar-props
  [sidebar-id sidebar-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-layouts.engine/layout-did-mount    sidebar-id sidebar-props))
                         :component-will-unmount (fn [_ _] (pretty-layouts.engine/layout-will-unmount sidebar-id sidebar-props))
                         :reagent-render         (fn [_ sidebar-props] [sidebar sidebar-id sidebar-props])}))

(defn view
  ; @description
  ; Sidebar style layout.
  ;
  ; @links Implemented accessories
  ; [Overlay](pretty-ui/cljs/pretty-accessories/api.html#overlay)
  ;
  ; @links Implemented layouts
  ; [Body](pretty-ui/cljs/pretty-layouts/api.html#body)
  ; [Footer](pretty-ui/cljs/pretty-layouts/api.html#footer)
  ; [Header](pretty-ui/cljs/pretty-layouts/api.html#header)
  ;
  ; @links Implemented properties
  ; [Animation properties](pretty-core/cljs/pretty-properties/api.html#animation-properties)
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Fullscreen properties](pretty-core/cljs/pretty-properties/api.html#fullscreen-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Structure properties](pretty-core/cljs/pretty-properties/api.html#structure-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) sidebar-id
  ; @param (map) sidebar-props
  ; Check out the implemented accessories.
  ; Check out the implemented layouts.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-layouts/sidebar.png)
  ; [sidebar {:body           {:content "My body" :fill-color :highlight :outer-height :parent}
  ;           :header         {:content "My header"}
  ;           :footer         {:content "My footer"}
  ;           :overlay        {:fill-color :invert}
  ;           :fill-color     :default
  ;           :inner-position :left
  ;           :inner-width    :micro
  ;           :outer-width    :parent}]
  ([sidebar-props]
   [view (random/generate-keyword) sidebar-props])

  ([sidebar-id sidebar-props]
   ; @note (tutorials#parameterizing)
   (fn [_ sidebar-props]
       (let [sidebar-props (pretty-presets.engine/apply-preset         sidebar-id sidebar-props)
             sidebar-props (sidebar.prototypes/sidebar-props-prototype sidebar-id sidebar-props)]
            [view-lifecycles sidebar-id sidebar-props]))))
