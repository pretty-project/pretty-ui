
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.layout-a.views
    (:require [x.app-components.api              :as components]
              [x.app-core.api                    :as a]
              [x.app-layouts.layout-a.helpers    :as layout-a.helpers]
              [x.app-layouts.layout-a.prototypes :as layout-a.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- layout-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ;  {:body (metamorphic-content)
  ;   :disabled? (boolean)(opt)
  ;   :footer (metamorphic-content)(opt)
  ;   :header (metamorphic-content)(opt)
  ;   :min-width (keyword)}
  [layout-id {:keys [body footer header] :as layout-props}]
  [:div.x-body-a (layout-a.helpers/layout-body-attributes layout-id layout-props)
                 [:div.x-body-a--content-structure
                   (if footer [:div.x-body-a--content-footer [components/content layout-id footer]]
                              [:div.x-body-a--content-body   [components/content layout-id body]])
                   (if header [:div.x-body-a--content-header [components/content layout-id header]])]

                 ; XXX#0093
                 ; - A layout-body sarkai border-radius tulajdonsággal vannak lekerekítve, amiből
                 ;   a {position: sticky} content-header alsó sarkai kilógnának, amikor a content-header
                 ;   lecsúszik a layout-body aljáig.
                 ; - Azért szükséges a content-tail spacert alkalmazni, hogy a {position: sticky}
                 ;   content-header ne tudjon a layout-body aljáig lecsúszni.
                 ; - {overflow: hidden} tulajdonsággal nem lehet eltűntetni a content-header kilógó sarkait,
                 ;   mert {overflow: hidden} elemben nem működne a {position: sticky} tulajdonság.
                 [:div.x-body-a--content-tail]])

(defn- layout-a
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ;  {:description (metamorphic-content)(opt)}
  [layout-id {:keys [description] :as layout-props}]
  [:<> (if description [:div.x-description-a (components/content {:content description})]
                       [:div.x-description-a--placeholder])
       [layout-body layout-id layout-props]
       [:div.x-footer-a]])

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
