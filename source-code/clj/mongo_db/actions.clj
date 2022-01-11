
(ns mongo-db.actions
    (:import org.bson.types.BSONTimestamp)
    (:require [mid-fruits.candy        :refer [param return]]
              [mid-fruits.gestures     :as gestures]
              [mid-fruits.keyword      :as keyword]
              [mid-fruits.vector       :as vector]
              [monger.collection       :as mcl]
              [mongo-db.engine         :as engine]
              [mongo-db.reader         :as reader]
              [x.server-core.api       :as a]
              [x.server-db.api         :as db]
              [x.server-dictionary.api :as dictionary]
              monger.joda-time))


;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(def ADAPTATION-ERROR "Document adaptation error!")



;; -- Preparing document ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- document<-last-order
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (namespacedd map) document
  ;
  ; @return (namespaced map)
  ;  {:namespace/order (integer)}
  [collection-name document]
  (let [last-order (reader/get-all-document-count collection-name)]
       (engine/document<-order document last-order)))

(defn- prepare-document
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (namespacedd map) document
  ; @param (map) options
  ;  {:ordered? (boolean)(opt)
  ;    Default: false
  ;   :prototype-f (function)(opt)}
  ;
  ; @return (namespaced map)
  ;  {:namespace/order (integer)}
  [collection-name document {:keys [ordered? prototype-f]}]
  (cond->> document ordered?    (document<-last-order collection-name)
                    prototype-f (prototype-f)))



;; -- Error handling ----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- insert-and-return!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (map) document
  ;  {:_id (string)}
  ;
  ; @return (map)
  [collection-name document]
  (let [database (a/subscribed [:mongo-db/get-connection])]
       (try (mcl/insert-and-return database collection-name document)
            (catch Exception e (println (str e "\n" {:collection-name collection-name :document document}))))))

(defn- save-and-return!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ;
  ; @return (?)
  [collection-name document]
  (let [database (a/subscribed [:mongo-db/get-connection])]
       (try (mcl/save-and-return database collection-name document)
            (catch Exception e (println (str e "\n" {:collection-name collection-name :document document}))))))

(defn- remove-by-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @return (?)
  [collection-name document-id]
  (let [database (a/subscribed [:mongo-db/get-connection])]
       (try (mcl/remove-by-id database collection-name document-id)
            (catch Exception e (println (str e "\n" {:collection-name collection-name :document-id document-id}))))))

(defn- update!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (map) conditions
  ; @param (map) document
  ; @param (map)(opt) options
  ;
  ; @return (?)
  ([collection-name conditions document])

  ([collection-name conditions document options]
   (let [database (a/subscribed [:mongo-db/get-connection])]
        (try (mcl/update database collection-name conditions document options)
             (catch Exception e (println (str e "\n" {:collection-name collection-name :conditions conditions
                                                      :document document :options options})))))))



;; -- Inserting document ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn insert-document!
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ;  {:namespace/id (string)(opt)}
  ;
  ; @example
  ;  (mongo-db/insert-document! "my-collection" {:my-namespace/id "my-document" ...})
  ;  =>
  ;  {:my-namespace/id "my-document" ...}
  ;
  ; @return (namespaced map)
  ;  {:namespace/id (string)}
  ([collection-name document]
   (insert-document! collection-name document {}))

  ([collection-name document options]
   (let [document (prepare-document collection-name document options)]
        (if-let [document (engine/adapt-input document)]
                (let [return (insert-and-return! collection-name document)]
                     (engine/adapt-output return))
                (println (str ADAPTATION-ERROR "\n" document))))))



;; -- Saving document ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-document!
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ;  {:namespace/id (string)(opt)}
  ; @param (map)(opt) options
  ;  {:ordered? (boolean)(opt)
  ;    Default: false
  ;   :prototype-f (function)(opt)}
  ;
  ; @example
  ;  (mongo-db/save-document! "my-collection" {:my-namespace/id "my-document" ...})
  ;  =>
  ;  {:my-namespace/id "my-document" ...}
  ;
  ; @return (namespaced map)
  ;  {:namespace/id (string)}
  ([collection-name document]
   (save-document! collection-name document {}))

  ([collection-name document options]
   (let [document (prepare-document collection-name document options)]
        (if-let [document (engine/adapt-input document)]
                (let [return (save-and-return! collection-name document)]
                     (engine/adapt-output return))
                (println (str ADAPTATION-ERROR "\n" document))))))



;; -- Adding document ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-document!
  ; Az add-document! függvény a számára azonosító nélkül átadott dokumentumot
  ; újonnan generált azonosítóval látja el az eltárolás előtt, míg a számára
  ; azonosítóval átadott dokumentumot annak azonosítójának változtatása nélkül
  ; tárolja el.
  ;
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ;  {:namespace/id (string)(opt)}
  ; @param (map)(opt) options
  ;  {:modifier-f (function)(opt)
  ;   :ordered? (boolean)
  ;    Default: false
  ;   :prototype-f (function)(opt)}
  ;
  ; @example
  ;  (mongo-db/add-document! "my-collection" {:my-namespace/my-keyword  :my-value
  ;                                           :my-namespace/your-string "your-value"})
  ;  =>
  ;  {:my-namespace/my-keyword  :my-value}
  ;   :my-namespace/your-string "your-value"
  ;   :my-namespace/id          "240b9771-05f2-4c06-8f64-177468e2c730"}
  ;
  ; @example
  ;  (mongo-db/add-document! "my-collection" {:my-namespace/my-keyword  :my-value
  ;                                           :my-namespace/your-string "your-value"
  ;                                           :my-namespace/id          "my-document"})
  ;  =>
  ;  {:my-namespace/my-keyword  :my-value}
  ;   :my-namespace/your-string "your-value"
  ;   :my-namespace/id          "my-document"}
  ;
  ; @return (namespaced map)
  ;  {:namespace/id (string)}
  ([collection-name document]
   (add-document! collection-name document {}))

  ([collection-name document {:keys [ordered? modifier-f prototype-f]}]
         ; A dokumentum sorrendbeli pozíciója egyenlő a hozzáadás előtti dokumentumok számával.
   (let [document-dex (reader/get-all-document-count collection-name)

         ; A prototype-f függvény azelőtt módosítja a dokumentumot, mielőtt az add-document! függvény
         ; végrehajtana rajta bármilyen változtatást.
         document (if (some?       prototype-f)
                      (prototype-f document)
                      (return      document))

                                   ; Ha a dokumentum rendezett dokumentumként kerül hozzáadásra,
                                   ; akkor szükséges a sorrendbeli pozícióját hozzáadni
;         document (cond-> document (boolean ordered?)
;                                   (engine/document<-order document-dex)))
                                   ; Ha a dokumentum nem tartalmaz azonosítót, akkor hozzáfűz
                                   ; egy generált azonosítót
                                   ;:assoc-id engine/document<-id)

         ; Ha a dokumentumot annak mentése előtt szeretnéd módosítani, használj modifier-f függvényt!
         document (if (some?      modifier-f)
                      (modifier-f document)
                      (return     document))]

        (save-document! collection-name document))))

; @usage
;  [:mongo-db/add-document! "my-collection" {...}]
(a/reg-handled-fx :mongo-db/add-document! add-document!)



;; -- Adding documents --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-documents!
  ; @param (string) collection-name
  ; @param (namespaced maps in vector) documents
  ;  [{:namespace/id (string)(opt)}]
  ; @param (map)(opt) options
  ;  {:modifier-f (function)(opt)
  ;   :ordered? (boolean)
  ;    Default: false
  ;   :prototype-f (function)(opt)}
  ;
  ; @usage
  ;  (mongo-db/add-documents! "my-collection" [{...} {...}])
  ;
  ; @return (namespaced map)
  ;  {:namespace/id (string)}
  ([collection-name documents]
   (add-documents! collection-name documents {}))

  ([collection-name documents options]
   (vector/->items documents #(add-document! collection-name % options))))

; @usage
;  [:mongo-db/add-documents! "my-collection" [{...} {...}]]
(a/reg-handled-fx :mongo-db/add-documents! add-documents!)



;; -- Upserting document ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn upsert-document!
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ;  {:namespace/id (string)}
  ; @param (map)(opt) options
  ;  {:ordered? (boolean)
  ;    Ha a dokumentum nem létezik a kollekcióban, akkor az upsert-document! függvény
  ;    hozzáadja azt a kollekcióhoz az add-document! függvény használatával.
  ;    Az {:ordered? ...} tulajdonság az add-document! függvény paramétere!
  ;    Default: false
  ;   :prototype-f (function)(opt)}
  ;
  ; @example
  ;  (mongo-db/upsert-document! "my-collection" {:my-namespace/my-keyword  :my-value
  ;                                              :my-namespace/your-string "your-value"
  ;                                              :my-namespace/id          "my-document"})
  ;  =>
  ;  {:my-namespace/my-keyword  :my-value}
  ;   :my-namespace/your-string "your-value"
  ;   :my-namespace/id          "my-document"}
  ;
  ; @return (namespaced map)
  ;  {:namespace/id (string)}
  ([collection-name document]
   (upsert-document! collection-name document {}))

  ([collection-name document {:keys [prototype-f] :as options}]
   (let [document-id (db/document->document-id document)]
        (if (reader/document-exists? collection-name document-id)
            ; If document exists ...
            (let [document (if (some?       prototype-f)
                               (prototype-f document)
                               (return      document))]
                 (save-document! collection-name document))
            ; If document NOT exists ...
            (add-document! collection-name document options)))))

; @usage
;  [:mongo-db/upsert-document! "my-collection" {:my-namespace/id "my-document"}]
(a/reg-handled-fx :mongo-db/upsert-document! upsert-document!)



;; -- Updating document -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-document!
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ;  {:namespace/id (string)}
  ; @param (map)(opt) options
  ;  {:prototype-f (function)(opt)}
  ;
  ; @example
  ;  (mongo-db/update-document! "my-collection" {:my-namespace/my-keyword  :my-value
  ;                                              :my-namespace/your-string "your-value"
  ;                                              :my-namespace/id          "my-document"})
  ;  =>
  ;  {:my-namespace/my-keyword  :my-value}
  ;   :my-namespace/your-string "your-value"
  ;   :my-namespace/id          "my-document"}
  ;
  ; @return (namespaced map)
  ;  {:namespace/id (string)}
  ([collection-name document]
   (update-document! collection-name document {}))

  ([collection-name document {:keys [prototype-f]}]
   (let [document-id (db/document->document-id document)]
        (if (reader/document-exists? collection-name document-id)
            ; If document exists ...
            (let [document (if (some?       prototype-f)
                               (prototype-f document)
                               (return      document))]
                 (save-document! collection-name document))
            ; If document NOT exists ...
            (println "Document does not exists error" collection-name document-id)))))

; @usage
;  [:mongo-db/update-document! "my-collection" {:my-namespace/id "my-document"}]
(a/reg-handled-fx :mongo-db/update-document! update-document!)



;; -- Applying document -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn apply-document!
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (map)(opt) options
  ;  {:prototype-f (function)(opt)}
  ;
  ; @example
  ;  (mongo-db/apply-document! "my-collection")
  ([collection-name document-id update-f])

  ([collection-name document-id update-f {:keys [prototype-f]}]))



;; -- Merging document --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn merge-document!
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ;  {:namespace/id (string)}
  ; @param (map)(opt) options
  ;  {:prototype-f (function)(opt)}
  ;
  ; @example
  ;  (mongo-db/merge-document! "my-collection" {:my-namespace/my-keyword  :my-value
  ;                                             :my-namespace/your-string "your-value"
  ;                                             :my-namespace/id          "my-document"})
  ;  =>
  ;  {:my-namespace/my-keyword  :my-value}
  ;   :my-namespace/your-string "your-value"
  ;   :my-namespace/id          "my-document"}
  ;
  ; @return (namespaced map)
  ;  {:namespace/id (string)}
  ([collection-name document]
   (merge-document! collection-name document {}))

  ([collection-name document {:keys [prototype-f]}]
   (let [document-id (db/document->document-id document)]
        (if-let [stored-document (reader/get-document-by-id collection-name document-id)]
                ; If document exists ...
                (let [document (merge stored-document document)
                      document (if (some?       prototype-f)
                                   (prototype-f document)
                                   (return      document))]
                     (save-document! collection-name document))
                ; If document NOT exists ...
                (println "Document does not exists error" collection-name document-id)))))

; @usage
;  [:mongo-db/merge-document! "my-collection" {:my-namespace/id "my-document"}]
(a/reg-handled-fx :mongo-db/merge-document! merge-document!)



;; -- Merging documents -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn merge-documents!
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ;  {:namespace/id (string)}
  ; @param (map)(opt) options
  ;  {:prototype-f (function)(opt)}
  ;
  ; @usage
  ;  (mongo-db/merge-documents! "my-collection" [{...} {...}])
  ;
  ; @return (namespaced maps in vector)
  ([collection-name documents]
   (merge-documents! collection-name documents {}))

  ([collection-name documents options]
   (vector/->items documents #(merge-document! collection-name % options))))

; @usage
;  [:mongo-db/merge-documents! "my-collection" [{...} {...}]]
(a/reg-handled-fx :mongo-db/merge-documents! merge-documents!)



;; -- Removing document -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- remove-unordered-document!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @return (string)
  [collection-name document-id]
  (let [database (a/subscribed [:mongo-db/get-connection])]
       (remove-by-id! collection-name document-id)
       (return        document-id)))

(defn- remove-ordered-document!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (string) document-id
  ;
  ; @return (string)
  [collection-name document-id]
  (let [document     (reader/get-document-by-id collection-name document-id)
        document-dex (engine/document->order document)
        namespace    (db/document->namespace document)
        order-key    (keyword/add-namespace  namespace :order)]
       ; A sorrendben a dokumentum után következő más dokumentumok sorrendbeli
       ; pozíciójának értékét eggyel csökkenti
       (update! collection-name {order-key {"$gt" document-dex}}
                                {"$inc" {order-key -1}}
                                {:multi true})
       (remove-unordered-document! collection-name document-id)))

(defn remove-document!
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (map)(opt) options
  ;  {:ordered? (boolean)
  ;    Default: false}
  ;
  ; @example
  ;  (mongo-db/remove-document "my-collection" "my-document")
  ;  =>
  ;  "my-document"
  ;
  ; @return (string)
  ([collection-name document-id]
   (remove-document! collection-name document-id {}))

  ([collection-name document-id {:keys [ordered?]}]
   (if ordered? (remove-ordered-document!   collection-name document-id)
                (remove-unordered-document! collection-name document-id))))

; @usage
;  [:mongo-db/remove-document! "my-collection" "my-document"]
(a/reg-handled-fx :mongo-db/remove-document! remove-document!)



;; -- Removing documents ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn remove-documents!
  ; @param (string) collection-name
  ; @param (strings in vector) document-ids
  ; @param (map)(opt) options
  ;  {:ordered? (boolean)
  ;    Default: false}
  ;
  ; @example
  ;  (mongo-db/remove-documents! "my-collection" ["my-document" "your-document"])
  ;  =>
  ;  ["my-document" "your-document"]
  ;
  ; @return (string)
  ([collection-name document-ids]
   (remove-documents! collection-name document-ids {}))

  ([collection-name document-ids options]
   (vector/->items document-ids #(remove-document! collection-name % options))))

; @usage
;  [:mongo-db/remove-documents! "my-collection" ["my-document" "your-document"]]
(a/reg-handled-fx :mongo-db/remove-document! remove-document!)



;; -- Duplicating document ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-document-copy-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (map) options
  ;  {:label-key (namespaced keyword)
  ;   :language-id (keyword)}
  ;
  ; @return (string)
  [collection-name document-id {:keys [label-key language-id]}]
  ; A dokumentumról készült másolat :label-key tulajdonságként átadott kulcsának
  ; értéke a label-suffix toldalék értékével kerül kiegészítése
  (let [document            (reader/get-document-by-id collection-name document-id)
        all-documents       (reader/find-all-documents collection-name)
        label-suffix        (dictionary/looked-up :copy {:language-id language-id})
        document-label      (get document label-key)
        all-document-labels (mapv label-key all-documents)]
       (gestures/item-label->duplicated-item-label document-label all-document-labels label-suffix)))

(defn- duplicate-unordered-document!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (map) options
  ;  {:label-key (namespaced keyword)
  ;   :language-id (keyword)
  ;   :modifier-f (function)(opt)
  ;   :prototype-f (function)(opt)}
  ;
  ; @return (namespaced map)
  ;  {:namespace/id (string)}
  [collection-name document-id {:keys [label-key modifier-f prototype-f] :as options}]
  (let [document  (reader/get-document-by-id collection-name document-id)
        namespace (db/document->namespace document)
        id-key    (keyword/add-namespace  namespace :id)

        ; A prototype-f függvény azelőtt módosítja a dokumentumot, mielőtt a duplicate-ordered-document!
        ; függvény végrehajtana rajta bármilyen változtatást.
        document-copy (if (some?       prototype-f)
                          (prototype-f document)
                          (return      document))

        ; A label-suffix toldalék értékével kiegészített címke asszociálása
        document-copy (if (some? label-key)
                          (let [copy-label (get-document-copy-label collection-name document-id options)]
                               (assoc document-copy label-key copy-label))
                          (return document-copy))

        ; Az eredeti dokumentum azonosítójának eltávolítása a másolatból
        document-copy (dissoc document-copy id-key)

        ; Ha a dokumentumot annak mentése előtt szeretnéd módosítani, használj modifier-f függvényt!
        document-copy (if (some?      modifier-f)
                          (modifier-f document-copy)
                          (return     document-copy))]

       (add-document! collection-name document-copy)))

(defn- duplicate-ordered-document!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (map) options
  ;  {:label-key (namespaced keyword)
  ;   :modifier-f (function)(opt)
  ;   :prototype-f (function)(opt)}
  ;
  ; @return (namespaced map)
  ;  {:namespace/id (string)}
  [collection-name document-id {:keys [label-key modifier-f prototype-f] :as options}]
  (let [document     (reader/get-document-by-id collection-name document-id)
        document-dex (db/get-document-value  document :order)
        namespace    (db/document->namespace document)
        id-key       (keyword/add-namespace  namespace :id)

        ; Ha a dokumentum nem rendelkezik {:namespace/order ...} tulajdonsággal ...
        document-copy-dex (try (inc document-dex)
                               (catch Exception e (println "Document corrupted error" collection-name document-id)))

        ; A prototype-f függvény azelőtt módosítja a dokumentumot, mielőtt a duplicate-ordered-document!
        ; függvény végrehajtana rajta bármilyen változtatást.
        document-copy (if (some?       prototype-f)
                          (prototype-f document)
                          (return      document))

        ; A label-suffix toldalék értékével kiegészített címke asszociálása
        document-copy (if (some? label-key)
                          (let [copy-label (get-document-copy-label collection-name document-id options)]
                               (assoc document-copy label-key copy-label))
                          (return document-copy))

                                   ; Az eredeti dokumentum azonosítójának eltávolítása a másolatból
;        document-copy (-> document (dissoc id-key)
                                   ; A sorrendbeli pozíció értékének hozzáadása a másolathoz
;                                   (engine/document<-order document-copy-dex)))

        ; Ha a dokumentumot annak mentése előtt szeretnéd módosítani, használj modifier-f függvényt!
        document-copy (if (some?      modifier-f)
                          (modifier-f document-copy)
                          (return     document-copy))]


       (let [namespace (db/document->namespace document)
             order-key (keyword/add-namespace  namespace :order)]
            ; A sorrendben a dokumentum után következő más dokumentumok sorrendbeli
            ; pozíciójának értékét eggyel növeli
            (update! collection-name {order-key {"$gt" document-copy-dex}}
                                     {"$inc" {order-key 1}}
                                     {:multi true}))

       (add-document! collection-name document-copy)))

(defn duplicate-document!
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (map) options
  ;  {:label-key (namespaced keyword)(opt)
  ;    A dokumentum melyik kulcsának értékéhez fűzze hozzá a "copy" kifejezést
  ;    Only w/ {:language-id ...}
  ;   :language-id (keyword)(opt)
  ;    Milyen nyelven használja a "copy" kifejezést a névhez hozzáfűzéskor
  ;    Only w/ {:label-key ...}
  ;   :modifier-f (function)(opt)
  ;   :ordered? (boolean)(opt)
  ;    Default: false
  ;   :prototype-f (function)(opt)}
  ;
  ; @example
  ;  (mongo-db/duplicate-document! "my-collection" "my-document")
  ;  =>
  ;  {:my-namespace/id "..." :my-namespace/label "My document"}
  ;
  ; @example
  ;  (mongo-db/duplicate-document! "my-collection" "my-document" {:label-key   :my-namespace/label
  ;                                                               :language-id :en})
  ;  =>
  ;  {:my-namespace/id "..." :my-namespace/label "My document copy"}
  ;
  ; @return (namespaced map)
  ;  {:namespace/id (string)}
  ([collection-name document-id]
   (duplicate-document! collection-name document-id {:ordered? false}))

  ([collection-name document-id {:keys [ordered?] :as options}]
   (if ordered? (duplicate-ordered-document!   collection-name document-id options)
                (duplicate-unordered-document! collection-name document-id options))))



;; -- Reordering collection ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reorder-documents!
  ; @param (string) collection-name
  ; @param (vectors in vector) document-order
  ;  [[(string) document-id
  ;    (integer) document-dex]]
  ;
  ; @usage
  ;  (mongo-db/reorder-documents "my-collection" [["my-document" 1] ["your-document" 2]])
  ;
  ; @return (vectors in vector)
  [collection-name document-order]
  (let [namespace (reader/get-collection-namespace collection-name)
        order-key (str (name namespace) "/order")]
       (vector/->items document-order (fn [[document-id document-dex]]
                                          (update! collection-name {:_id document-id}
                                                                   {"$set" {order-key document-dex}})))))
