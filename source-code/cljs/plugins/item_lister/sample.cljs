
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.sample
    (:require [plugins.item-lister.api :as item-lister]
              [re-frame.api            :as r]
              [x.app-core.api          :as core]
              [x.app-elements.api      :as elements]))




;; -- Szerver-oldali beállítás ------------------------------------------------
;; ----------------------------------------------------------------------------

; A plugin beállításához mindenképpen szükséges a szerver-oldali
; [:item-lister/init-lister! ...] eseményt használni!



;; -- Listaelemek megjelenítése előszűréssel ----------------------------------
;; ----------------------------------------------------------------------------

; A {:prefilter {...}} tulajdonság használatával beállíthatod, hogy a listában
; csak az előszűrésnek megfelelő elemek jelenjenek meg.
(defn my-filtered-body
  []
  [item-lister/body :my-lister {:list-element [:div "My item"]
                                :prefilter    {:my-type/color "red"}}])



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



;; -- Előre kijelölt elemek ---------------------------------------------------
;; ----------------------------------------------------------------------------

; Az item-lister/body komponensének {:selected-items [...]} paraméterként lehetséges
; azon elemek azonosítóit átadni, amely elemeket szeretnéd, ha a letöltődésük után
; ki lennének jelölve.



;; -- Listaelemek frissítése a kollekció változása után -----------------------
;; ----------------------------------------------------------------------------

; Az [:item-lister/reload-items! ...] esemény újra letölti az összes elemet az aktuális
; beállításokkal. Így lehetséges az adatok kliens-oldalon megjelenített változatát aktualizálni
; a szerver-oldali változathoz.
(r/reg-event-fx :reload-my-items!
  [:item-lister/reload-items! :my-lister])



;; -- A plugin használata alapbeállításokkal ----------------------------------
;; ----------------------------------------------------------------------------

(defn my-lister
  []
  [item-lister/body :my-lister {:list-element [:div "My item"]}])



;; -- Kifejezések hozzáadaása a szótárhoz -------------------------------------
;; ----------------------------------------------------------------------------

; Ha a body komponens default-order-by tulajdonságánál vagy az [:item-lister/choose-order-by! ...]
; esemény order-by-options tulajdonságánal egyedi értékeket is használtál, akkor
; ne felejtsd el a szótárhoz adni a megfelelő kifejezéseket!
; Pl.: A :my-order/ascending értékhez tartozó kifejezés: {:by-my-order-ascending {...}}
(core/reg-lifecycles! ::lifecycles
  {:on-app-boot [:dictionary/add-terms! {:by-my-order-ascending {:en "..." :hu "..."}}]})



;; -- Pathom lekérés használata az elemek első letöltésekor -------------------
;; ----------------------------------------------------------------------------

; Az item-lister plugin body komponensének {:query [...]} tulajdonságaként
; átadott Pathom lekérés vektor az elemek első letöltődésekor küldött lekéréssel
; összefűzve elküldésre kerül.
(defn my-query
  []
  [item-lister/body :my-lister {:list-element [:div "My item"]
                                :query        [:my-query]}])
