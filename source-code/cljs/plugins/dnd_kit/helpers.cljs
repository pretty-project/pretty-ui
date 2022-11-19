
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.dnd-kit.helpers
    (:require [candy.api             :refer [return]]
              [map.api               :refer [dissoc-in]]
              [plugins.dnd-kit.state :as state]
              [plugins.reagent.api   :as reagent]
              [time.api              :as time]
              [vector.api            :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sortable-did-mount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  ;  {:items (vector)}
  [sortable-id {:keys [item-id-f items]}]
  ; XXX#0080
  ; 1.
  ; A dnd-kit.core/sortable-context komponens számára átadott térkép items tulajdonsága
  ; olyan vektor kell legyen, amelynek az elemei string típusok, vagy térkép típusok
  ; :id vagy :namespace/id kulccsal, amely kulcsok értékei string típusok.
  ; Pl.: ["my-id" "your-id"]
  ;      [{:id "my-id"} {:id "your-id"}]
  ;      [{:namespace/id "my-id"} {:namespace/id "your-id"}]
  ;
  ; 2.
  ; A plugins.dnd-kit/body komponens ...
  ; ... számára items tulajdonságként átadott vektor elemei ezzel szemben bármilyen típusúak lehetnek.
  ; ... a React-fába csatolódásakor eltárolja a számára items tulajdonságként átadott vektor ...
  ;     ... elemeiből az item-id-f tulajdonságként átadott függvénnyel kinyert azonosítókat
  ;         a state/SORTABLE-STATE atomban (item-order).
  ;     ... elemeit az item-id-f tulajdonságként átadott függvénnyel kinyert azonosítóikkal
  ;         azonosítva a state/SORTABLE-STATE atomban (sortable-items).
  ;
  ; 3.
  ; A dnd-kit.core/sortable-context komponens számára átadott térkép items tulajdonságaként
  ; a SORTABLE-STATE item-order vektora kerül átadásra, ami csak az elemek azonosítóit tartalmazza
  ; (string típusra alakítva!), ezért a dnd-kit.core/sortable-context komponensnek nem az elemekkel,
  ; hanem csak azok azonosítóival kell dolgoznia, így a plugins.dnd-kit/body komponens számára átadott
  ; elemek bármilyen típusúak lehetnek és nem szükséges az :id vagy :namespace/id kulcsot tartalmazniuk!
  (letfn [(f [state item]
             (if-let [item-id (-> item item-id-f str)]
                     (-> state (assoc-in  [:sortable-items item-id] item)
                               (update-in [:item-order] vector/conj-item item-id))
                     (return state)))]
         (swap! state/SORTABLE-STATE assoc sortable-id (reduce f {} items))))

(defn sortable-will-unmount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  [sortable-id _]
  ; BUG#0047
  ; Ha a dnd-kit plugin sortable komponensének component-will-unmount életciklusa
  ; késleltetés nélkül törölné ki a state/SORTABLE-STATE atomból az elemeket tartalmazó
  ; vektort, akkor a sortable-body komponensben alkalmazott if függvény a vektor
  ; törlésekor else ágra váltva lecsatolná a React-fából a dnd-context komponenst
  ; mielőtt annak minden életciklusa rendben lezajlana (nem tudom, mi történik a dnd-kit-ben)
  ; és a dnd-kit.core/DndContext React komponens a következő hibaüzenetet jelenítené
  ; meg a konzolon:
  ; Can't perform a React state update on an unmounted component. ...
  (letfn [(f [] (swap! state/SORTABLE-STATE dissoc sortable-id))]
         (time/set-timeout! f 50)))

(defn sortable-did-update-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (?) %
  [sortable-id %]
  (let [[_ sortable-props] (reagent/arguments %)]
       (sortable-did-mount-f sortable-id sortable-props)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-ordered-items
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  ;
  ; @return (vector)
  [sortable-id _]
  (letfn [(f [items item-id] (conj items (get-in @state/SORTABLE-STATE [sortable-id :sortable-items item-id])))]
         (let [item-order (get-in @state/SORTABLE-STATE [sortable-id :item-order])]
              (reduce f [] item-order))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn event->origin-dex
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  ; @param (?) event
  ;
  ; @return (integer)
  [sortable-id _ event]
  (let [item-order (get-in @state/SORTABLE-STATE [sortable-id :item-order])]
       (vector/item-first-dex item-order (aget event "active" "id"))))

(defn event->target-dex
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  ; @param (?) event
  ;
  ; @return (integer)
  [sortable-id _ event]
  (let [item-order (get-in @state/SORTABLE-STATE [sortable-id :item-order])]
       (vector/item-first-dex item-order (aget event "over" "id"))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn drag-start-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  ;  {:on-drag-start (function)(opt)}
  ; @param (?) event
  [sortable-id {:keys [on-drag-start] :as sortable-props} event]
  (if on-drag-start (on-drag-start sortable-id sortable-props))
  (let [grabbed-item-dex (get-in (js->clj (aget event "active")) ["data" "current" "sortable" "index"])]
       ;(println (js->clj event))
       ;(println "grabbed-item-dex:" grabbed-item-dex)
       (swap! state/SORTABLE-STATE assoc-in [sortable-id :grabbed-item] grabbed-item-dex)))

(defn drag-end-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sortable-id
  ; @param (map) sortable-props
  ;  {:on-drag-end (metamorphic-event)(opt)
  ;   :on-order-changed (metamorphic-event)(opt)}
  ; @param (?) event
  [sortable-id {:keys [on-drag-end on-order-changed] :as sortable-props} event]
  (let [origin-dex (event->origin-dex sortable-id sortable-props event)
        target-dex (event->target-dex sortable-id sortable-props event)]
    ; Check if dragged element is moved if so than reset the items order.
    ;(println "event:"      event)
    ;(println "origin-dex:" origin-dex)
    ;(println "target-dex:" target-dex)
    (if (not= origin-dex target-dex)
        (let [item-order (vector/move-item (get-in @state/SORTABLE-STATE [sortable-id :item-order]) origin-dex target-dex)]
             (swap! state/SORTABLE-STATE assoc-in [sortable-id :item-order] item-order)
             (if on-order-changed (let [reordered-items (get-ordered-items sortable-id sortable-props)]
                                       (on-order-changed sortable-id sortable-props reordered-items)))))
    (swap! state/SORTABLE-STATE dissoc-in [sortable-id :grabbed-item])
    (if on-drag-end (on-drag-end sortable-id sortable-props))))
