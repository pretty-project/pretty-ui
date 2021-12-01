

; A termékeket lehet group-olni: ez kell a tartozékcsoportokhoz
;
; És lehet több darab címkét hozzáadni {:product/tags ["..." "..."]}

; {:product/name     "..."
;  :product/tags     ["..." "..."]
;  :product/price    123)

;

; Meg kell oldani, hogy az árajánlat tudja, hogy egy termék jármű-e vagy kiegészítő!
; A products-ban lehet hozzáadni, új tulajdonságokat séma-editorral de ha ez is hozzáadható, akkor
; van egy emberi tényező ami veszélyes, mert az árjaánlatnak pontosan kell tudni, hogy melyik a primary-termék
; szal melyik maga a jármű
;
; A webhely a termékkategóriák alapján fogja felsorolni, hogy milyen termékek jelennek meg a kategóriákban
; A termékkategóriák és alkategóriák külön collection (mint a directories).
;
; Hogyan kapcsoljuk egy járműhöz a tartozékokat boolx-esen? A webhelyen felkell sorolni a választható
; tartozékokat.
;
; Modell/típus: product/subproduct

; product-categories collection
; products           collection
; product-groups     collection



(ns extensions.products.product-lister
    (:require [x.app-core.api        :as a :refer [r]]
              [x.app-details         :as details]
              [x.app-elements.api    :as elements]
              [x.app-environment.api :as environment]
              [x.app-layouts.api     :as layouts]
              [app-plugins.item-editor.api :as item-editor]
              [app-plugins.item-lister.api :as item-lister]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-item-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [db [_ _ {:client/keys [added-at]}]]
  {;:added-at          (r activities/get-actual-timestamp db added-at)
   :viewport-small?   (r environment/viewport-small?     db)})

(a/reg-sub ::get-item-props get-item-props)



;; -- Product-item components -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn product-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [item-dex product-props]
  [:div "product"])



;; -- Product-list header components ------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-list-desktop-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id view-props])

(defn- product-list-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [search-mode? viewport-small?] :as view-props}]
  (cond ; search-mode & small viewport
;        (and viewport-small? search-mode?)
;        [item-lister/search-header :products :product]
        ; small viewport
        ;(boolean viewport-small?)
        ;[product-list-mobile-header  surface-id view-props]
        ; large viewport
        :desktop-header
        [product-list-desktop-header surface-id view-props]))



;; -- Product-list body components --------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-list-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [:div
   [elements/menu-bar {:menu-items [{:label "All" :on-click []} {:label "Active" :color :muted :on-click []}
                                    {:label "Draft" :color :muted :on-click []} {:label "Archived" :color :muted :on-click []}]}]

   [item-lister/body :products :product
                     {:list-element #'product-item}]])



;; -- Product-list components -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id {:keys [description]}]
  [layouts/layout-a surface-id {:body   {:content    #'product-list-body}
                                :header {:content    #'product-list-header
                                         :subscriber [:item-lister/get-header-props :products]}
                                :description description}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :products/render-product-lister!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/set-surface! ::view {:content #'view}])
