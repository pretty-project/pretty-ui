
; WARNING
; Az x.ui.renderer.events névtér újraírása szükséges, a Re-Frame adatbázis
; események számának csökkentése érdekében!

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; x5 Clojure/ClojureScript web application engine
; https://monotech.hu/x5
;
; Copyright Adam Szűcs and other contributors - All rights reserved

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.renderer.events
    (:require [re-frame.api          :as r :refer [r]]
              [re-frame.db.api       :as r.db]
              [time.api              :as time]
              [vector.api            :as vector]
              [x.ui.renderer.helpers :as renderer.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-render-log!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (keyword) action-key
  ;
  ; @usage
  ; (r update-render-log! db :bubbles :my-bubble :render-requested-at)
  ;
  ; @return (map)
  [db [_ renderer-id element-id action-key]]
  (let [meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
       (assoc-in db [:x.ui meta-items-key :render-log element-id action-key]
                    (time/elapsed))))

(defn reserve-renderer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (map)
  [db [_ renderer-id]]
  (let [meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
       (assoc-in db [:x.ui meta-items-key :reserved?] true)))

(defn free-renderer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (map)
  [db [_ renderer-id]]
  (let [meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
       (assoc-in db [:x.ui meta-items-key :reserved?] false)))

(defn store-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;
  ; @return (map)
  [db [_ renderer-id element-id element-props]]
  (let [data-items-key (renderer.helpers/data-key renderer-id :data-items)
        data-order-key (renderer.helpers/data-key renderer-id :data-order)]
       (as-> db % (r update-render-log! % renderer-id  element-id :rendered-at)
                  (r r.db/set-item!     % [:x.ui data-items-key element-id] element-props)
                  (r r.db/apply-item!   % [:x.ui data-order-key] vector/move-item-to-last element-id))))

(defn remove-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ renderer-id element-id]]
  (let [data-items-key (renderer.helpers/data-key renderer-id :data-items)]
       (as-> db % (r update-render-log! % renderer-id  element-id :props-removed-at)
                  (r r.db/remove-item!  % [:x.ui data-items-key element-id]))))

(defn update-element-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;
  ; @return (map)
  [db [_ renderer-id element-id element-props]]
  (let [data-items-key (renderer.helpers/data-key renderer-id :data-items)]
       (as-> db % (r update-render-log! % renderer-id element-id :updated-at)
                  (r r.db/set-item!     % [:x.ui data-items-key element-id] element-props))))

(defn set-element-prop!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (keyword) prop-key
  ; @param (*) prop-value
  ;
  ; @return (map)
  [db [_ renderer-id element-id prop-key prop-value]]
  (let [data-items-key (renderer.helpers/data-key renderer-id :data-items)]
       (assoc-in db [:x.ui data-items-key element-id prop-key] prop-value)))

(defn stop-element-rendering!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Az element-props eltávolítása előtt, szükséges leállítani a UI elem renderelését.
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ renderer-id element-id]]
  (let [data-order-key (renderer.helpers/data-key renderer-id :data-order)]
       (update-in db [:x.ui data-order-key] vector/remove-item element-id)))

(defn render-element-later!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;
  ; @return (map)
  [db [_ renderer-id element-id element-props]]
  (let [meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
       (update-in db [:x.ui meta-items-key :rendering-queue]
                     vector/conj-item [element-id element-props])))

(defn trim-rendering-queue!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (map)
  [db [_ renderer-id]]
  (let [meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
       (update-in db [:x.ui meta-items-key :rendering-queue]
                     vector/remove-nth-item 0)))

(defn empty-rendering-queue!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (map)
  [db [_ renderer-id]]
  (let [meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
       (assoc-in db [:x.ui meta-items-key :rendering-queue] [])))

(defn mark-element-as-invisible!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ renderer-id element-id]]
  (let [meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
       (update-in db [:x.ui meta-items-key :invisible-elements]
                     vector/conj-item element-id)))

(defn unmark-element-as-invisible!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ renderer-id element-id]]
  (let [meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
       (update-in db [:x.ui meta-items-key :invisible-elements]
                     vector/remove-item element-id)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :x.ui/reserve-renderer! reserve-renderer!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :x.ui/remove-element! remove-element!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :x.ui/set-element-prop! set-element-prop!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :x.ui/stop-element-rendering! stop-element-rendering!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :x.ui/render-element-later! render-element-later!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :x.ui/trim-rendering-queue! trim-rendering-queue!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :x.ui/empty-rendering-queue! empty-rendering-queue!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :x.ui/mark-element-as-invisible! mark-element-as-invisible!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :x.ui/unmark-element-as-invisible! unmark-element-as-invisible!)
