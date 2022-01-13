
(ns mongo-db.actions
   ; WARNING! DEPRECATED! DO NOT USE!
   ;(:import org.bson.types.BSONTimestamp)
   ; WARNING! DEPRECATED! DO NOT USE!
    (:require monger.joda-time
              [mid-fruits.candy        :refer [param return]]
              [mid-fruits.gestures     :as gestures]
              [mid-fruits.keyword      :as keyword]
              [mid-fruits.vector       :as vector]
              [monger.collection       :as mcl]
              [monger.operators        :refer :all]
              [monger.result           :as mrt]
              [mongo-db.adaptation     :as adaptation]
              [mongo-db.engine         :as engine]
              [mongo-db.errors         :as errors]
              [mongo-db.reader         :as reader]
              [x.server-core.api       :as a]
              [x.server-db.api         :as db]
              [x.server-dictionary.api :as dictionary]))



;; -- Preparing document ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- prepare-document-order
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ;
  ; @return (namespaced map)
  ;  {:namespace/order (integer)}
  [collection-name document]
  (if-let [namespace (db/document->namespace document)]
          (let [order-key (keyword/add-namespace namespace :order)]
               (if-let ; Ha a dokumentum tartalmaz :namespace/order tulajdonságot ...
                       [order (get document order-key)]
                       ; ... növeli a dokumentum :namespace/order tulajdonságának értékét.
                       (update document order-key inc)
                       ; ... a dokumentum kapja a kollekció sorrendbeli utolsó pozícióját.
                       (let [last-order (reader/get-all-document-count collection-name)]
                            (assoc document order-key last-order))))
          (throw (Exception. errors/MISSING-NAMESPACE-ERROR))))

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
  (try (cond->> document ordered?    (prepare-document-order collection-name)
                         prototype-f (prototype-f))
       (catch Exception e (println (str e "\n" {:collection-name collection-name :document document})))))



;; -- Error handling ----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- insert-and-return!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (map) document
  ;  {"_id" (org.bson.types.ObjectId object)}
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
  ;  {"_id" (org.bson.types.ObjectId object)}
  ;
  ; @return (map)
  [collection-name document]
  (let [database (a/subscribed [:mongo-db/get-connection])]
       (try (mcl/save-and-return database collection-name document)
            (catch Exception e (println (str e "\n" {:collection-name collection-name :document document}))))))

(defn- remove-by-id!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (org.bson.types.ObjectId object) document-id
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
  ;  {"_id" (org.bson.types.ObjectId object)(opt)}
  ; @param (map) document
  ; @param (map)(opt) options
  ;  {:multi (boolean)(opt)
  ;    Default: false
  ;   :upsert (boolean)(opt)
  ;    Default: false}
  ;
  ; @return (com.mongodb.WriteResult object)
  ([collection-name conditions document]
   (update! collection-name conditions document {}))

  ([collection-name conditions document options]
   (let [database (a/subscribed [:mongo-db/get-connection])]
        (try (mcl/update database collection-name conditions document options)
             (catch Exception e (println (str e "\n" {:collection-name collection-name :conditions conditions
                                                      :document document :options options})))))))

(defn- upsert!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (map) conditions
  ;  {"_id" (org.bson.types.ObjectId object)(opt)}
  ; @param (map) document
  ; @param (map)(opt) options
  ;  {:multi (boolean)(opt)
  ;    Default: false}
  ;
  ; @return (com.mongodb.WriteResult object)
  ([collection-name conditions document]
   (upsert! collection-name conditions document {}))

  ([collection-name conditions document options]
   (let [database (a/subscribed [:mongo-db/get-connection])]
        (try (mcl/upsert database collection-name conditions document options)
             (catch Exception e (println (str e "\n" {:collection-name collection-name :conditions conditions
                                                      :document document :options options})))))))



;; -- Inserting document ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn insert-document!
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ;  {:namespace/id (string)(opt)}
  ; @param (map)(opt) options
  ;  {:ordered? (boolean)(opt)
  ;    Default: false
  ;   :prototype-f (function)(opt)}
  ;
  ; @example
  ;  (mongo-db/insert-document! "my-collection" {:namespace/id "MyObjectId" ...})
  ;  =>
  ;  {:namespace/id "MyObjectId" ...}
  ;
  ; @return (namespaced map)
  ;  {:namespace/id (string)}
  ([collection-name document]
   (insert-document! collection-name document {}))

  ([collection-name document options]
   (if-let [document (prepare-document collection-name document options)]
           (if-let [document (adaptation/insert-input document)]
                   (let [result (insert-and-return! collection-name document)]
                        (adaptation/insert-output result))))))

; @usage
;  [:mongo-db/insert-document! "my-collection" {:namespace/id "MyObjectId" ...}]
(a/reg-handled-fx :mongo-db/insert-document! insert-document!)



;; -- Inserting documents -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn insert-documents!
  ; @param (string) collection-name
  ; @param (namespaced maps in vector) documents
  ;  [{:namespace/id (string)(opt)}]
  ; @param (map)(opt) options
  ;  {:ordered? (boolean)(opt)
  ;    Default: false
  ;   :prototype-f (function)(opt)}
  ;
  ; @example
  ;  (mongo-db/insert-documents! "my-collection" [{:namespace/id "12ab3cd4efg5h6789ijk0420" ...}])
  ;  =>
  ;  [{:namespace/id "12ab3cd4efg5h6789ijk0420" ...}]
  ;
  ; @return (namespaced maps in vector)
  ;  [{:namespace/id (string)}]
  ([collection-name documents]
   (insert-documents! collection-name documents {}))

  ([collection-name documents options]
   (vector/->items documents #(insert-document! collection-name % options))))

; @usage
;  [:mongo-db/insert-documents! "my-collection" [{:namespace/id "MyObjectId" ...}]]
(a/reg-handled-fx :mongo-db/insert-documents! insert-documents!)



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
  ;  (mongo-db/save-document! "my-collection" {:namespace/id "MyObjectId" ...})
  ;  =>
  ;  {:namespace/id "MyObjectId" ...}
  ;
  ; @return (namespaced map)
  ;  {:namespace/id (string)}
  ([collection-name document]
   (save-document! collection-name document {}))

  ([collection-name document options]
   (if-let [document (prepare-document collection-name document options)]
           (if-let [document (adaptation/save-input document)]
                   (let [result (save-and-return! collection-name document)]
                        (adaptation/save-output result))))))

; @usage
;  [:mongo-db/save-document! "my-collection" {:namespace/id "MyObjectId" ...}]
(a/reg-handled-fx :mongo-db/save-document! save-document!)



;; -- Saving documents --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn save-documents!
  ; @param (string) collection-name
  ; @param (namespaced maps in vector) documents
  ;  [{:namespace/id (string)(opt)}]
  ; @param (map)(opt) options
  ;  {:ordered? (boolean)(opt)
  ;    Default: false
  ;   :prototype-f (function)(opt)}
  ;
  ; @example
  ;  (mongo-db/save-documents! "my-collection" [{:namespace/id "MyObjectId" ...}])
  ;  =>
  ;  [{:namespace/id "MyObjectId" ...}]
  ;
  ; @return (namespaced maps in vector)
  ;  [{:namespace/id (string)}]
  ([collection-name documents]
   (save-documents! collection-name documents {}))

  ([collection-name documents options]
   (vector/->items documents #(save-document! collection-name % options))))

; @usage
;  [:mongo-db/save-documents! "my-collection" [{:namespace/id "MyObjectId" ...}]]
(a/reg-handled-fx :mongo-db/save-documents! save-documents!)



;; -- Updating document -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-document!
  ; @param (string) collection-name
  ; @param (namespaced map) conditions
  ; @param (namespaced map) document
  ; @param (map)(opt) options
  ;  {:prototype-f (function)(opt)}
  ;
  ; @usage
  ;  (mongo-db/update-document! "my-collection" {:namespace/score 100} {:namespace/score 0})
  ;
  ; @return (boolean)
  ([collection-name conditions document]
   (update-document! collection-name conditions document {}))

  ([collection-name conditions document options]
   (boolean (if-let [document (prepare-document collection-name document options)]
                    (if-let [conditions (adaptation/update-conditions conditions)]
                            (if-let [document (adaptation/update-input document)]
                                    (let [result (update! collection-name conditions document {:multi false :upsert false})]
                                         (mrt/updated-existing? result))))))))

; @usage
;  [:mongo-db/update-document! "my-collection" {:namespace/score 100} {:namespace/score 0}]
(a/reg-handled-fx :mongo-db/update-document! update-document!)



;; -- Updating document -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-documents!
  ; @param (string) collection-name
  ; @param (namespaced map) conditions
  ; @param (namespaced map) document
  ; @param (map)(opt) options
  ;  {:prototype-f (function)(opt)}
  ;
  ; @usage
  ;  (mongo-db/update-documents! "my-collection" {:namespace/score 100} {:namespace/score 0})
  ;
  ; @return (boolean)
  ([collection-name conditions document]
   (update-documents! collection-name conditions document {}))

  ([collection-name conditions document options]
   (boolean (if-let [document (prepare-document collection-name document options)]
                    (if-let [conditions (adaptation/update-conditions conditions)]
                            (if-let [document (adaptation/update-input document)]
                                    ; WARNING! DO NOT USE!
                                    ; java.lang.IllegalArgumentException: Replacements can not be multi
                                    (let [result (update! collection-name conditions document {:multi true :upsert false})]
                                         (mrt/updated-existing? result))))))))

; @usage
;  [:mongo-db/update-documents! "my-collection" {:namespace/score 100} {:namespace/score 0}]
(a/reg-handled-fx :mongo-db/update-documents! update-documents!)



;; -- Upserting document ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn upsert-document!
  ; @param (string) collection-name
  ; @param (namespaced map) conditions
  ; @param (namespaced map) document
  ; @param (map)(opt) options
  ;  {:prototype-f (function)(opt)}
  ;
  ; @usage
  ;  (mongo-db/upsert-document! "my-collection" {:namespace/score 100} {:namespace/score 0})
  ;
  ; @return (namespaced map)
  ([collection-name conditions document]
   (upsert-document! collection-name conditions document {}))

  ([collection-name conditions document options]
   (if-let [document (prepare-document collection-name document options)]
           (if-let [conditions (adaptation/upsert-conditions conditions)]
                   (if-let [document (adaptation/upsert-input document)]
                           (let [result (upsert! collection-name conditions document {:multi false})]
                                (mrt/acknowledged? result)))))))

; @usage
;  [:mongo-db/upsert-document! "my-collection" {:namespace/score 100} {:namespace/score 0}]
(a/reg-handled-fx :mongo-db/upsert-document! upsert-document!)



;; -- Upserting documents -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn upsert-documents!
  ; @param (string) collection-name
  ; @param (namespaced map) conditions
  ; @param (namespaced map) document
  ; @param (map)(opt) options
  ;  {:prototype-f (function)(opt)}
  ;
  ; @usage
  ;  (mongo-db/upsert-documents! "my-collection" {:namespace/score 100} {:namespace/score 0})
  ;
  ; @return (namespaced map)
  ([collection-name conditions document]
   (upsert-documents! collection-name conditions document {}))

  ([collection-name conditions document options]
   (if-let [document (prepare-document collection-name document options)]
           (if-let [conditions (adaptation/upsert-conditions conditions)]
                   (if-let [document (adaptation/upsert-input document)]
                           ; WARNING! DO NOT USE!
                           ; java.lang.IllegalArgumentException: Replacements can not be multi
                           (let [result (upsert! collection-name conditions document {:multi true})]
                                (mrt/acknowledged? result)))))))

; @usage
;  [:mongo-db/upsert-documents! "my-collection" {:namespace/score 100} {:namespace/score 0}]
(a/reg-handled-fx :mongo-db/upsert-documents! upsert-documents!)
























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

  ([collection-name documents options]))
   ;(vector/->items documents #(add-document! collection-name % options))))

; @usage
;  [:mongo-db/add-documents! "my-collection" [{...} {...}]]
(a/reg-handled-fx :mongo-db/add-documents! add-documents!)





;; -- Updating document -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-document!_
  ; @param (string) collection-name
  ; @param (namespaced map) document
  ;  {:namespace/id (string)}
  ; @param (map)(opt) options
  ;  {:prototype-f (function)(opt)}
  ;
  ; @example
  ;  (mongo-db/update-document! "my-collection" {:namespace/my-keyword  :my-value
  ;                                              :namespace/your-string "your-value"
  ;                                              :namespace/id          "MyObjectId"})
  ;  =>
  ;  {:namespace/my-keyword  :my-value}
  ;   :namespace/your-string "your-value"
  ;   :namespace/id          "MyObjectId"}
  ;
  ; @return (namespaced map)
  ;  {:namespace/id (string)}
  ([collection-name document]
   (update-document!_ collection-name document {}))

  ([collection-name document {:keys [prototype-f]}]
   (let [document-id (db/document->document-id document)]
        (if (reader/document-exists? collection-name document-id)
            ; If document exists ...
            (let [document (if (some?       prototype-f)
                               (prototype-f document)
                               (return      document))]
                 (save-document! collection-name document))
            ; If document NOT exists ...
            (println errors/DOCUMENT-DOES-NOT-EXISTS-ERROR collection-name document-id)))))

; @usage
;  [:mongo-db/update-document! "my-collection" {:namespace/id "MyObjectId"}]
(a/reg-handled-fx :mongo-db/update-document!_ update-document!_)



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
  ;  (mongo-db/merge-document! "my-collection" {:namespace/my-keyword  :my-value
  ;                                             :namespace/your-string "your-value"
  ;                                             :namespace/id          "MyObjectId"})
  ;  =>
  ;  {:namespace/my-keyword  :my-value}
  ;   :namespace/your-string "your-value"
  ;   :namespace/id          "MyObjectId"}
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
                (println errors/DOCUMENT-DOES-NOT-EXISTS-ERROR collection-name document-id)))))

; @usage
;  [:mongo-db/merge-document! "my-collection" {:namespace/id "MyObjectId"}]
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
  (let [database (a/subscribed [:mongo-db/get-connection])
        document-id (adaptation/document-id-input document-id)]
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
  ;  (mongo-db/remove-document "my-collection" "MyObjectId")
  ;  =>
  ;  "MyObjectId"
  ;
  ; @return (string)
  ([collection-name document-id]
   (remove-document! collection-name document-id {}))

  ([collection-name document-id {:keys [ordered?]}]
   (if ordered? (remove-ordered-document!   collection-name document-id)
                (remove-unordered-document! collection-name document-id))))

; @usage
;  [:mongo-db/remove-document! "my-collection" "MyObjectId"]
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
  ;  (mongo-db/remove-documents! "my-collection" ["MyObjectId" "your-document"])
  ;  =>
  ;  ["MyObjectId" "your-document"]
  ;
  ; @return (string)
  ([collection-name document-ids]
   (remove-documents! collection-name document-ids {}))

  ([collection-name document-ids options]
   (vector/->items document-ids #(remove-document! collection-name % options))))

; @usage
;  [:mongo-db/remove-documents! "my-collection" ["MyObjectId" "your-document"]]
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
        all-documents       (reader/get-all-documents  collection-name)
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
  ;
  ; @return (namespaced map)
  [collection-name document-id options]
  (if-let [document (reader/get-document-by-id collection-name document-id)]
          (if-let [document-copy (prepare-document collection-name document options)]
                  (if-let [document-copy (adaptation/duplicate-input document-copy)]
                          (let [result (insert-and-return! collection-name document-copy)]
                               (adaptation/duplicate-output result))))
          (throw (Exception. errors/DOCUMENT-DOES-NOT-EXISTS-ERROR))))

(defn- duplicate-ordered-document!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (map) options
  ;
  ; @return (namespaced map)
  [collection-name document-id options]

          ; WARNING! NEM TÖRTÉNIK MEG A DOKUMENTUMOK POZÍCIÓJÁNAK NÖVELÉSE!
          ; A sorrendben a másolat után következő dokumentumok sorrendbeli pozíciójának növelése
  (letfn [(increase-documents-order! [collection-name document-copy]
                                     (let [namespace         (db/document->namespace document-copy)
                                           order-key         (keyword/add-namespace namespace :order)
                                           document-copy-dex (get document-copy order-key)
                                           result (update! collection-name {order-key {"$gt" document-copy-dex}}
                                                                           {"$inc" {order-key 1}}
                                                                           {:multi true})]
                                          (if-not (mrt/acknowledged? result)
                                                  (throw (Exception. errors/UPDATING-DOCUMENTS-ORDER-FAILURE)))))]
         (if-let [document (reader/get-document-by-id collection-name document-id)]
                 (if-let [document-copy (prepare-document collection-name document options)]
                         (do (increase-documents-order! collection-name document-copy)
                             (if-let [document-copy (adaptation/duplicate-input document-copy)]
                                     (let [result (insert-and-return! collection-name document-copy)]
                                          (adaptation/duplicate-output result)))))
                 (throw (Exception. errors/DOCUMENT-DOES-NOT-EXISTS-ERROR)))))

(defn duplicate-document!
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (map) options
  ;  {:label-key (namespaced keyword)(opt)
  ;    A dokumentum melyik kulcsának értékéhez fűzze hozzá a "copy" kifejezést
  ;    Only w/ {:language-id ...}
  ;   :language-id (keyword)(opt)
  ;    Milyen nyelven használja a "copy" kifejezést a hozzáfűzéskor
  ;    Only w/ {:label-key ...}
  ;   :ordered? (boolean)(opt)
  ;    Default: false
  ;   :prototype-f (function)(opt)}
  ;
  ; @example
  ;  (mongo-db/duplicate-document! "my-collection" "MyObjectId")
  ;  =>
  ;  {:namespace/id "MyObjectId" :namespace/label "My document"}
  ;
  ; @example
  ;  (mongo-db/duplicate-document! "my-collection" "MyObjectId" {:label-key   :namespace/label
  ;                                                              :language-id :en})
  ;  =>
  ;  {:namespace/id "MyObjectId" :namespace/label "My document copy"}
  ;
  ; @return (namespaced map)
  ;  {:namespace/id (string)}
  ([collection-name document-id]
   (duplicate-document! collection-name document-id {:ordered? false}))

  ([collection-name document-id {:keys [ordered?] :as options}]
   (try (if ordered? (duplicate-ordered-document!   collection-name document-id options)
                     (duplicate-unordered-document! collection-name document-id options))
        (catch Exception e (println (str e "\n" {:collection-name collection-name :document-id document-id}))))))



;; -- Reordering collection ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reorder-documents!
  ; @param (string) collection-name
  ; @param (vectors in vector) document-order
  ;  [[(string) document-id
  ;    (integer) document-dex]]
  ;
  ; @usage
  ;  (mongo-db/reorder-documents "my-collection" [["MyObjectId" 1] ["your-document" 2]])
  ;
  ; @return (vectors in vector)
  [collection-name document-order]
  (let [namespace (reader/get-collection-namespace collection-name)
        order-key (str (name namespace) "/order")]
       (vector/->items document-order (fn [[document-id document-dex]]
                                          (update! collection-name {:_id document-id}
                                                                   {"$set" {order-key document-dex}})))))
