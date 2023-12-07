
; WARNING
; Az x.ui.renderer.subs névtér újraírása szükséges, a Re-Frame adatbázis
; események számának csökkentése érdekében!

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; x5 Clojure/ClojureScript web application engine
; https://monotech.hu/x5
;
; Copyright Adam Szűcs and other contributors - All rights reserved

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.renderer.subs
    (:require [fruits.vector.api     :as vector]
              [re-frame.api          :as r :refer [r]]
              [x.ui.renderer.config  :as renderer.config]
              [x.ui.renderer.helpers :as renderer.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-invisible-element-ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (vector)
  [db [_ renderer-id]]
  (let [meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
       (get-in db [:x.ui meta-items-key :invisible-elements])))

(defn get-visible-element-order
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (vector)
  [db [_ renderer-id]]
  ; WARNING!
  ; A visible-element order vektor külön kell majd tárolni!
  ; Ne a feliratkozások számítsák ki az értékét!
  (let [rendered-element-order (r get-rendered-element-order db renderer-id)
        invisible-element-ids  (r get-invisible-element-ids  db renderer-id)]
       (vector/remove-items rendered-element-order invisible-element-ids)))

(defn any-element-rendered?
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [rendered-element-order (r get-rendered-element-order db renderer-id)]
       (vector/nonempty? rendered-element-order)))

(defn any-element-invisible?
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [invisible-element-ids (r get-invisible-element-ids db renderer-id)]
       (vector/nonempty? invisible-element-ids)))

(defn any-element-visible?
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [visible-element-order (r get-visible-element-order db renderer-id)]
       (vector/nonempty? visible-element-order)))

(defn no-visible-elements?
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [any-element-visible? (r any-element-visible? db renderer-id)]
       (not any-element-visible?)))

(defn get-lower-visible-element-id
  ; @param (keyword) renderer-id
  ;
  ; @return (keyword)
  [db [_ renderer-id]]
  (vector/first-item (r get-visible-element-order db renderer-id)))

(defn get-max-elements-rendered
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (integer)
  [db [_ renderer-id]]
  (let [meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
       (get-in db [:x.ui meta-items-key :max-elements-rendered])))

(defn max-elements-reached?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [max-elements-rendered (r get-max-elements-rendered db renderer-id)
        visible-element-order (r get-visible-element-order db renderer-id)]
       (vector/count? visible-element-order max-elements-rendered)))

(defn get-renderer-queue-behavior
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (keyword)
  [db [_ renderer-id]]
  (let [meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
       (get-in db [:x.ui meta-items-key :queue-behavior])))

(defn pushed-rendering-enabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [queue-behavior (r get-renderer-queue-behavior db renderer-id)]
       (= queue-behavior :push)))

(defn ignore-rendering?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [queue-behavior (r get-renderer-queue-behavior db renderer-id)]
       (and (= queue-behavior :ignore)
            (r max-elements-reached? db renderer-id))))

(defn renderer-reserved?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
       (get-in db [:x.ui meta-items-key :reserved?])))

(defn renderer-free?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
       (not (get-in db [:x.ui meta-items-key :reserved?]))))

(defn get-element-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ renderer-id element-id]]
  (let [data-items-key (renderer.helpers/data-key renderer-id :data-items)]
       (get-in db [:x.ui data-items-key element-id])))

(defn get-element-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (keyword) prop-key
  ;
  ; @return (*)
  [db [_ renderer-id element-id prop-key]]
  (let [data-items-key (renderer.helpers/data-key renderer-id :data-items)]
       (get-in db [:x.ui data-items-key element-id prop-key])))

(r/reg-sub :x.ui.renderer/get-element-prop get-element-prop)

(defn reveal-element-animated?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id]]
  (let [reveal-animated? (r get-element-prop db renderer-id element-id :reveal-animated?)]
       (boolean reveal-animated?)))

(defn hide-element-animated?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id]]
  (let [hide-animated? (r get-element-prop db renderer-id element-id :hide-animated?)]
       (boolean hide-animated?)))

(defn element-rendered?
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id]]
  (let [rendered-element-order (r get-rendered-element-order db renderer-id)]
       (vector/contains-item? rendered-element-order element-id)))

(defn element-invisible?
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id]]
  (let [invisible-element-ids (r get-invisible-element-ids db renderer-id)]
       (vector/contains-item? invisible-element-ids element-id)))

(defn element-visible?
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id]]
  (and      (r element-rendered?  db renderer-id element-id)
       (not (r element-invisible? db renderer-id element-id))))

(defn rerender-same?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
       (get-in db [:x.ui meta-items-key :rerender-same?])))

(defn get-rerender-delay
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (ms)
  [db [_ renderer-id element-id]]
  (if (r hide-element-animated? db renderer-id element-id)
      (+ renderer.config/HIDE-ANIMATION-TIMEOUT
         renderer.config/RENDER-DELAY-OFFSET)
      (return renderer.config/RENDER-DELAY-OFFSET)))

(defn get-pushing-delay
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (ms)
  [db [_ renderer-id]]
  (let [lower-visible-element-id (r get-lower-visible-element-id db renderer-id)]
       (if (r hide-element-animated? db renderer-id lower-visible-element-id)
           (+ renderer.config/HIDE-ANIMATION-TIMEOUT
              renderer.config/RENDER-DELAY-OFFSET)
           (return renderer.config/RENDER-DELAY-OFFSET))))

(defn get-queue-rendering-delay
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (ms)
  [db [_ renderer-id element-id]]
  (if (r  hide-element-animated? db renderer-id element-id)
      (+  renderer.config/HIDE-ANIMATION-TIMEOUT
          renderer.config/RENDER-DELAY-OFFSET)
      (-> renderer.config/RENDER-DELAY-OFFSET)))

(defn get-destroying-delay
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (ms)
  [db [_ renderer-id element-id]]
  (if (r  hide-element-animated? db renderer-id element-id)
      (+  renderer.config/HIDE-ANIMATION-TIMEOUT
          renderer.config/DESTROY-DELAY-OFFSET)
      (-> renderer.config/DESTROY-DELAY-OFFSET)))

(defn render-element-now?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id _]]
  (and (r renderer-free? db renderer-id)
       (or      (r pushed-rendering-enabled? db renderer-id)
           (not (r max-elements-reached?     db renderer-id)))))

(defn get-rendering-queue
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (vectors in vector)
  ; [[(keyword) element-id
  ;   (map) element-props]
  ;  [...]]
  [db [_ renderer-id]]
  (let [meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
       (get-in db [:x.ui meta-items-key :rendering-queue])))

(defn get-next-rendering
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (vector)
  ; [(keyword) element-id
  ;  (map) element-props]
  [db [_ renderer-id]]
  (let [rendering-queue (r get-rendering-queue db renderer-id)]
       (vector/first-item rendering-queue)))

(defn render-element?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id element-props]]
  (let [element-rendered? (r element-rendered? db renderer-id element-id)
        rerender-same?    (r rerender-same?    db renderer-id)]
       (or rerender-same? (not  element-rendered?)

                          ; TEMP
                          ; A render-element? függvény azért lett a requested-rendering-element!
                          ; esemény megtörténések alapfeltétele, mert bizonyos esetekben,
                          ; pl. surface felületeknél egy adott felülett renderelő eseménye
                          ; többször megtörténhet, amig a felület ki van renderelve.
                          ; Pl.: A felhasználó egy elemet néz egy felületen, amit egy listából
                          ;      választott ki, és a felület része a lista, ahol újra ki tud
                          ;      választani elemeket, amik elindítják a felület renderelő eseményét,
                          ;      "feleslegesen", mivel a felület már ki van renderelve.
                          ;
                          ; De sajnos ez megakadályozta, hogy egy bubble elem többször megjelenhessen
                          ; ugynazzal az azonosítóval, ezért ki lett egészitve ezzel a feltétellel:
                          ; Ha megváltozott az element-props, akkor engedi az újboli renderelést.
                          ;
                          ; A bubble elem ha ujra megjelenik (frissül/update-el), akkor egy animacio
                          ; is van rajta, és ez fontos része a UX-nek!
                          ;
                          ; Amit nem tudok, hogy az eltárolt element-props térképbe kerül-e bármilyen
                          ; meta adat, mert akkor ennek a not= összehasonlitásnak nem sok értelme van.
                          ;
                          ; Most ez igy ideiglenesen OK, de ezzel a megoldással pl. a bubble-ök nem
                          ; animálódnak, ha ugyanazzal az ID-val és element-props térképpel vannak
                          ; meghívva, pedig fontos lenne!
                          ; Pl.: rákattintasz többször egy gombra, hogy "Vágólapra helyezés/másolás/..."
                          ;      és minden kattintásnál fontos lenne, a bubble animálva update-eljen,
                          ;      még akkor is ha nem változott a megjelenitett tartalma.
                          (not= element-props (r get-element-props db renderer-id element-id)))))

(defn rerender-element?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id _]]
  (let [element-rendered? (r element-rendered? db renderer-id element-id)
        rerender-same?    (r rerender-same?    db renderer-id)]
       (and element-rendered? rerender-same?)))

(defn update-element-animated?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:update-animated? (boolean)(opt)}
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id {:keys [update-animated?]}]]
  (and      (r element-rendered? db renderer-id element-id)
       (not (r rerender-same?    db renderer-id))
       (boolean update-animated?)))

(defn update-element-static?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:update-animated? (boolean)(opt)}
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id {:keys [update-animated?]}]]
  (and      (r element-rendered? db renderer-id element-id)
       (not (r rerender-same?    db renderer-id))
       (not update-animated?)))

(defn push-element?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id _]]
  (and      (r max-elements-reached?     db renderer-id)
            (r pushed-rendering-enabled? db renderer-id)
       (not (r element-rendered?         db renderer-id element-id))))

(defn render-element-animated?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:reveal-animated? (boolean)(opt)}
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id {:keys [reveal-animated?]}]]
  (and (not (r max-elements-reached? db renderer-id))
       (not (r element-rendered?     db renderer-id element-id))
       (boolean reveal-animated?)))

(defn render-element-static?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:reveal-animated? (boolean)(opt)}
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id {:keys [reveal-animated?]}]]
  (and (not (r max-elements-reached? db renderer-id))
       (not (r element-rendered?     db renderer-id element-id))
       (not reveal-animated?)))

(defn destroy-element-animated?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id]]
  (and (r element-visible?       db renderer-id element-id)
       (r hide-element-animated? db renderer-id element-id)))

(defn destroy-element-static?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id]]
  (and      (r element-visible?       db renderer-id element-id)
       (not (r hide-element-animated? db renderer-id element-id))))

(defn get-visible-elements-destroying-event-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (maps in vector)
  ; [{:ms (ms)
  ;   :dispatch (Re-Frame metamorphic-event)}]
  [db [_ renderer-id]]
  (let [visible-element-order (r get-visible-element-order db renderer-id)]
       (letfn [(f0 [event-list dex element-id]
                   ; Az időzített esemény-lista aktuális elemének eltűntetési eseményének időértéke,
                   ; az utolsó (előző) elem időértéke ({:ms ...}), összeadva az előző elem eltűnéséhez
                   ; szükséges idővel
                   (if (zero? dex)
                       ; The first destroying event in the list ...
                       [{:dispatch [:x.ui/destroy-element! renderer-id element-id] :ms 0}]
                       ; The other destroying events in the list ...
                       (let [prev-element-id  (get-in event-list [(dec dex) :dispatch 2])
                             prev-event-delay (get-in event-list [(dec dex) :ms])
                             destroying-delay (r get-destroying-delay db renderer-id prev-element-id)]
                            (conj event-list {:dispatch [:x.ui/destroy-element! renderer-id element-id]
                                              :ms       (+ prev-event-delay destroying-delay)}))))]
              (reduce-kv f0 [] visible-element-order))))

(defn get-visible-elements-destroying-duration
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (ms)
  [db [_ renderer-id]]
  (let [visible-element-order (r get-visible-element-order db renderer-id)]
       (letfn [(f0 [duration element-id]
                   (let [destroying-delay (r get-destroying-delay db renderer-id element-id)]
                        (+ duration destroying-delay)))]
              (reduce f0 0 visible-element-order))))

(defn get-render-log
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (keyword) action-key
  ;
  ; @usage
  ; (r renderer/get-render-log db :bubbles :my-bubble :render-requested-at)
  ;
  ; @return (ms)
  [db [_ renderer-id element-id action-key]]
  (let [meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
       (get-in db [:x.ui meta-items-key :render-log element-id action-key])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :x.ui/get-element-order get-element-order)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :x.ui/get-element-props get-element-props)
