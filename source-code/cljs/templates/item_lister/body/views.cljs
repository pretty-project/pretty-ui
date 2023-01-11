
(ns templates.item-lister.body.views
    (:require [components.api                        :as components]
              [elements.api                          :as elements]
              [engines.item-lister.api               :as item-lister]
              [re-frame.api                          :as r]
              [templates.item-lister.body.helpers    :as body.helpers]
              [templates.item-lister.body.prototypes :as body.prototypes]
              [x.components.api                      :as x.components]

              ; TEMP#0880
              ; A dnd-kit.api helyett az azt alkalmazó engines.item-sorter.api
              ; engine-t szükséges használni!
              [dnd-kit.api :as dnd-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn placeholder-label
  ; @param (keyword) lister-id
  ; @param (map) body-props
  [_ _]
  [elements/label ::placeholder-label
                  {:color            :highlight
                   :content          :no-items-to-show
                   :font-size        :xs
                   :font-weight      :bold
                   :horizontal-align :center
                   :outdent          {:all :xs}}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- list-item-wrapper
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ; {:items-path (vector)
  ;  :list-item-element (component or symbol)}
  [lister-id {:keys [items-path list-item-element] :as body-props} item-dex item-id]
  ; BUG#0551
  ; Az item-lister engine body komponensének :items-path tulajdonsága a komponens
  ; :component-did-mount életciklusának megtörténése után érhető el a Re-Frame
  ; adatbázisban. Ahhoz, hogy az első olyan rendereléskor, amikor az elemek és azok
  ; sorrendje már elérhető (megtörént az első letöltés) ez ne okozzon villanást,
  ; az elemek késve történő megjelenése miatt, az elemek az :item-order meta-adat
  ; alapján vannak felsorolva és az item-lister template body komponensének
  ; :items-path paraméterének használatával vannak kiolvasva a Re-Frame adatbázisból,
  ; mivel ezek nem függnek egyetlen komponens életciklusától sem.
  (let [item @(r/subscribe [:x.db/get-item (conj items-path item-id)])]
       [list-item-element lister-id body-props item-dex item]))

(defn- static-item-list
  ; @param (keyword) lister-id
  ; @param (map) body-props
  [lister-id body-props]
  (let [item-order @(r/subscribe [:item-lister/get-item-order lister-id])]
       (letfn [(f [item-list item-dex item-id]
                  (conj item-list [list-item-wrapper lister-id body-props item-dex item-id]))]
              (reduce-kv f [:<>] item-order))))

(defn- sortable-item-list
  ; @param (keyword) lister-id
  ; @param (map) body-props
  [lister-id body-props]
  (let [item-order @(r/subscribe [:item-lister/get-item-order lister-id])]
       [dnd-kit/body lister-id
                     {:items            item-order
                      :item-element     [list-item-wrapper lister-id body-props]
                      :on-order-changed (body.helpers/on-order-changed-f lister-id body-props)}]))

(defn- item-list-structure
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ; {:sortable? (boolean)(opt)}
  [lister-id {:keys [sortable?] :as body-props}]
  (if sortable? [sortable-item-list lister-id body-props]
                [static-item-list   lister-id body-props]))

(defn- item-list
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ; {:item-list-header (component or symbol)(opt)}
  [lister-id {:keys [item-list-header] :as body-props}]
  [:div#t-item-lister--item-list [:div#t-item-lister--item-list-body   [item-list-structure  lister-id body-props]]
                                 [:div#t-item-lister--item-list-header [x.components/content lister-id item-list-header]]])

(defn- body-structure
  ; @param (keyword) lister-id
  ; @param (map) body-props
  [lister-id body-props]
  [:<> [item-list              lister-id body-props]
       [item-lister/downloader lister-id body-props]
       (if @(r/subscribe [:item-lister/display-error? lister-id])
            [components/error-content {:content :the-content-you-opened-may-be-broken}])
       (if @(r/subscribe [:item-lister/display-placeholder? lister-id])
            [placeholder-label lister-id body-props])
       (if @(r/subscribe [:item-lister/display-ghost? lister-id])
            [components/ghost-view {:layout :item-list :item-count 3}])])

(defn body
  ; A komponens további paraméterezését az engines.item-lister.api/body komponens
  ; dokumentácójában találod!
  ;
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ; {:default-order-by (namespaced keyword)(opt)
  ;   Default: :modified-at/descending
  ;  :list-item-element (component or symbol)
  ;  :list-item-header (component or symbol)(opt)
  ;  :items-path (vector)
  ;  :on-order-changed (metamorphic-event)(opt)
  ;   This event takes the reordered items as its last parameter
  ;   W/ {:sortable? true}
  ;  :sortable? (boolean)(opt)
  ;   Default: false}
  ;
  ; @usage
  ; [body {...}]
  ;
  ; @usage
  ; [body :my-lister {...}]
  ;
  ; @usage
  ; (defn my-item-element [lister-id body-props item-dex item] ...)
  ; [body :my-lister {:item-element  #'my-item-element}]
  ;
  ; @usage
  ; (defn my-item-list-header  [lister-id body-props] ...)
  ; (defn my-list-item-element [lister-id body-props item-dex item drag-props] ...)
  ; [body :my-lister {:item-list-header  #'my-item-list-header
  ;                   :list-item-element #'my-list-item-element
  ;                   :sortable?         true}]
  [lister-id body-props]
  (let [body-props (body.prototypes/body-props-prototype lister-id body-props)]
       [:div#t-item-lister--body [body-structure lister-id body-props]]))
