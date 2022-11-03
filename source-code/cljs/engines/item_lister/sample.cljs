
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.sample
    (:require [elements.api            :as elements]
              [engines.item-lister.api :as item-lister]
              [mid-fruits.vector       :as vector]
              [re-frame.api            :as r]
              [x.app-core.api          :as x.core]))



;; -- Szerver-oldali beállítás ------------------------------------------------
;; ----------------------------------------------------------------------------

; Az engine beállításához mindenképpen szükséges a szerver-oldali
; [:item-lister/init-lister! ...] eseményt használni!



;; -- Listaelemek megjelenítése előszűréssel ----------------------------------
;; ----------------------------------------------------------------------------

; A {:prefilter {...}} tulajdonság használatával beállíthatod, hogy a listában
; csak az előszűrésnek megfelelő elemek jelenjenek meg.
(defn my-filtered-body
  []
  [item-lister/body :my-lister {:prefilter {:my-type/color "red"}}])



;; -- Listaelemek szűrése eseménnyel ------------------------------------------
;; ----------------------------------------------------------------------------

; Az [:item-lister/filter-items! ...] esemény használatával lehetséges szűrési feltételeket beállítani
(r/reg-event-fx :use-my-filter!
  [:item-lister/filter-items! :my-lister {:$or [{:my-type/id "my-item"} {:your-type/id "your-item"}]}])



;; -- Szűrők használata -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-filters
  []
  (let [my-filter-event [:item-lister/filter-items! :my-lister {}]]
       [elements/menu-bar {:menu-items [{:label "My filter" :on-click my-filter-event}]}]))

(defn my-lister-with-filters
  []
  [:<> [my-filters]
       [item-lister/body :my-lister {}]])



;; -- Listaelemek frissítése a kollekció változása után -----------------------
;; ----------------------------------------------------------------------------

; Az [:item-lister/reload-items! ...] esemény újra letölti az összes elemet az aktuális
; beállításokkal. Így lehetséges az adatok kliens-oldalon megjelenített változatát aktualizálni
; a szerver-oldali változathoz.
(r/reg-event-fx :reload-my-items!
  [:item-lister/reload-items! :my-lister])



;; -- Listaelemek sorrendjének aktualizálása a szerveren ----------------------
;; ----------------------------------------------------------------------------

; Az [:item-lister/reorder-items! ...] esemény ...
; ... a letöltött elemeket a reordered-items vektorban tárolt sorrendjük szerint
;     ellátja a body-komponens számára :order-key paraméterként átadott kulcshoz
;     rendelt sorrendi értékkel.
; ... elküldi a szerver-oldali my-handler/reorder-items! mutation függvény számára
;     az újrarendezett elemeket.
;
; (def db {:downloaded-items [{:id "my-item"} {:id "your-item"}]})
; (r/dispatch [:reorder-my-items! [{:id "your-item"} {:id "my-item"}]])
; =>
; [{:id "your-item" :order 0} {:id "my-item" :order 1}]
(r/reg-event-fx :reorder-my-items!
  (fn [_ _]
      (let [downloaded-items (r item-lister/get-downloaded-items db :my-lister)
            reordered-items  (vector/move-item downloaded-items 0 5)]
           [:item-lister/reorder-items! :my-lister reordered-items])))



;; -- Az engine használata alapbeállításokkal ----------------------------------
;; ----------------------------------------------------------------------------

(defn my-list-element
  [lister-id items]
  ; A lista-elemek React-kulcsának tartalmaznia kell az adott elem indexét, hogy a lista-elemek
  ; törlésekor a megmaradó elemek alkalmazkodjanak az új indexükhöz!
  (letfn [(f [item-list item-dex {:keys [id] :as item}]
             (conj item-list ^{:key (str id item-dex)} [:div "My item"]))]
         (reduce-kv f [:<>] items)))

(defn my-lister
  []
  [item-lister/body :my-lister {:list-element #'my-list-element}])



;; -- Kifejezések hozzáadaása a szótárhoz -------------------------------------
;; ----------------------------------------------------------------------------

; Ha a body komponens default-order-by tulajdonságánál vagy az [:item-lister/choose-order-by! ...]
; esemény order-by-options tulajdonságánal egyedi értékeket is használtál, akkor
; ne felejtsd el a szótárhoz adni a megfelelő kifejezéseket!
; Pl.: A :my-order/ascending értékhez tartozó kifejezés: {:by-my-order-ascending {...}}
(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot [:dictionary/add-terms! {:by-my-order-ascending {:en "..." :hu "..."}}]})



;; -- Pathom lekérés használata az elemek első letöltésekor -------------------
;; ----------------------------------------------------------------------------

; Az item-lister engine body komponensének {:query [...]} tulajdonságaként
; átadott Pathom lekérés vektor az elemek első letöltődésekor küldött lekéréssel
; összefűzve elküldésre kerül.
(defn my-query
  []
  [item-lister/body :my-lister {:query [:my-query]}])
