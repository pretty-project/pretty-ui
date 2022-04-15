
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.layout-a.views
    (:require [reagent.api                       :as reagent]
              [x.app-components.api              :as components]
              [x.app-core.api                    :as a]
              [x.app-layouts.layout-a.helpers    :as layout-a.helpers]
              [x.app-layouts.layout-a.prototypes :as layout-a.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- layout-content-header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ;  {:header (metamorphic-content)(opt)}
  [layout-id {:keys [header]}]
  ; BUG#4009
  ; A header komponensben megjelenített tartalom x-tengelyen való görgethetőségét biztosító
  ; {overflow-x: auto} beállítást az #x-layout-a--content-header--scroll-container
  ; elem valósítja meg, így az #x-layout-a--content-header[data-sticky="true"]::before
  ; pszeudoelem az #x-layout-a--content-header elem területén kívül is megjeleníthető.
  ; Ha az #x-layout-a--content-header elem {overflow-x: auto} beállítással jelenne meg,
  ; akkor az ... pszeudoelem nem "lóghatna ki" az #x-layout-a--content-header elem területéről.
  [:<> [:div#x-layout-a--content-header--sensor]
       [:div#x-layout-a--content-header [:div#x-layout-a--content-header--scroll-container [components/content layout-id header]]]])

(defn- layout-content-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  [layout-id layout-props]
  (reagent/lifecycles {:component-did-mount    (fn [] (layout-a.helpers/content-header-did-mount-f    layout-id))
                       :component-will-unmount (fn [] (layout-a.helpers/content-header-will-unmount-f layout-id))
                       :reagent-render         (fn [] [layout-content-header-structure layout-id layout-props])}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- layout-content-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ;  {:body (metamorphic-content)}
  [layout-id {:keys [body]}]
  [:div#x-layout-a--content-body [components/content layout-id body]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- layout-content-footer-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ;  {:footer (metamorphic-content)(opt)}
  [layout-id {:keys [footer]}]
  [:<> [:div#x-layout-a--content-footer--sensor]
       [:div#x-layout-a--content-footer [components/content layout-id footer]]])

(defn- layout-content-footer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  [layout-id layout-props]
  ; XXX#0093
  ; - A layout-body sarkai border-radius tulajdonsággal vannak lekerekítve, amiből
  ;   a {position: sticky} pozícionálású content-header alsó sarkai kilógnának,
  ;   amikor a content-header lecsúszik a layout-body aljáig.
  ;
  ; - Azért szükséges a content-tail elemet alkalmazni, hogy a {position: sticky}
  ;   pozícionálású content-header ne tudjon a layout-body aljáig lecsúszni.
  ;
  ; - {overflow: hidden} tulajdonsággal nem lehet eltűntetni a content-header kilógó sarkait,
  ;   mert {overflow: hidden} elemben nem működne a {position: sticky} beállítás.
  (reagent/lifecycles {:component-did-mount    (fn [] (layout-a.helpers/content-footer-did-mount-f    layout-id))
                       :component-will-unmount (fn [] (layout-a.helpers/content-footer-will-unmount-f layout-id))
                       :reagent-render         (fn [] [layout-content-footer-structure layout-id layout-props])}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- layout-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ;  {:footer (metamorphic-content)(opt)
  ;   :header (metamorphic-content)(opt)}
  [layout-id {:keys [footer header] :as layout-props}]
  [:div#x-layout-a (layout-a.helpers/layout-body-attributes layout-id layout-props)
                   [:div#x-layout-a--content-structure [layout-content-body   layout-id layout-props]
                                            (if header [layout-content-header layout-id layout-props])]
                   (if footer [layout-content-footer layout-id layout-props]
                              [:div#x-layout-a--content-tail])])

(defn- layout-a
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ;  {:description (metamorphic-content)(opt)}
  [layout-id {:keys [description] :as layout-props}]
  [:<> (if description [:div#x-layout-a--description (components/content {:content description})]
                       [:div#x-layout-a--description {:data-placeholder true}])
       [layout-content layout-id layout-props]
       [:div#x-layout-a--footer]])

(defn layout
  ; @param (keyword)(opt) layout-id
  ; @param (map) layout-props
  ;  {:body (metamorphic-content)
  ;   :class (keyword or keywords in vector)(opt)
  ;   :description (metamorphic-content)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :footer (metamorphic-content)(opt)
  ;   :header (metamorphic-content)(opt)
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right
  ;    Default: :center
  ;   :min-width (keyword)(opt)
  ;    :m, :l, :xl, :xxl
  ;    Default: :l
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [layouts/layout-a {...}]
  ;
  ; @usage
  ;  [layouts/layout-a :my-layout {...}]
  ([layout-props]
   [layout (a/id) layout-props])

  ([layout-id layout-props]
   (let [layout-props (layout-a.prototypes/layout-props-prototype layout-props)]
        [layout-a layout-id layout-props])))
