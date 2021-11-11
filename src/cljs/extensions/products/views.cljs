
(ns extensions.products.views
    (:require [x.app-core.api     :as a :refer [r]]
              [x.app-details      :as details]
              [x.app-elements.api :as elements]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [:div])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :products/render-product-list!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/set-surface! ::view {:content #'view}])

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:extensions/add-item-list-route! "products" "product"]}



; A termékeket lehet group-olni: ez kell a tartozékcsoportokhoz
;
; És lehet több darab címkét hozzáadni {:product/tags ["..." "..."]}

 {:product/name     "..."
  :product/tags     ["..." "..."]
  :product/price    123})

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
