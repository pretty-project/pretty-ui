
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

(defn- drop!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ;
  ; @return (?)
  [collection-name]
  (let [database (a/subscribed [:mongo-db/get-connection])]
       (try (mcl/drop database collection-name)
            (catch Exception e (println e (str e "\n" {:collection-name collection-name}))))))

(defn- insert-and-return!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (map) document
  ;  {"_id" (org.bson.types.ObjectId object)}
  ;
  ; @return (namespaced map)
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
  ; @return (namespaced map)
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
  ; @param (map) query
  ;  {"_id" (org.bson.types.ObjectId object)(opt)}
  ; @param (map) document
  ; @param (map)(opt) options
  ;  {:multi (boolean)(opt)
  ;    Default: false
  ;   :upsert (boolean)(opt)
  ;    Default: false}
  ;
  ; @return (com.mongodb.WriteResult object)
  ([collection-name query document]
   (update! collection-name query document {}))

  ([collection-name query document options]
   (let [database (a/subscribed [:mongo-db/get-connection])]
        (try (mcl/update database collection-name query document options)
             (catch Exception e (println (str e "\n" {:collection-name collection-name :query   query
                                                      :document        document        :options options})))))))

(defn- upsert!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (map) query
  ;  {"_id" (org.bson.types.ObjectId object)(opt)}
  ; @param (map) document
  ; @param (map)(opt) options
  ;  {:multi (boolean)(opt)
  ;    Default: false}
  ;
  ; @return (com.mongodb.WriteResult object)
  ([collection-name query document]
   (upsert! collection-name query document {}))

  ([collection-name query document options]
   (let [database (a/subscribed [:mongo-db/get-connection])]
        (try (mcl/upsert database collection-name query document options)
             (catch Exception e (println (str e "\n" {:collection-name collection-name :query   query
                                                      :document        document        :options options})))))))



;; -- Reordering following documents ------------------------------------------
;; ----------------------------------------------------------------------------

(defn- reorder-following-documents!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (map) options
  ;  {:operation (keyword)
  ;    :decrease, :increase}
  ;
  ; @return (namespaced map)
  [collection-name document-id {:keys [operation]}]
  ; WARNING! NEM TÖRTÉNIK MEG A DOKUMENTUMOK POZÍCIÓJÁNAK ELTOLÁSA!
  (if-let [document (reader/get-document-by-id collection-name document-id)]
          (let [namespace    (db/document->namespace document)
                order-key    (keyword/add-namespace  namespace :order)
                document-dex (get document order-key)
                ; A sorrendben a dokumentum után következő dokumentumok sorrendbeli pozíciójának eltolása
                result (update! collection-name {order-key {"$gt" document-dex}}
                                                (case operation :increase {"$inc" {order-key 1}}
                                                                :decrease {"$dec" {order-key 1}})
                                                {:multi true})]
               (if-not (mrt/acknowledged? result)
                       (throw (Exception. errors/REORDER-DOCUMENTS-FAILED))))
          (throw (Exception. errors/DOCUMENT-DOES-NOT-EXISTS-ERROR))))



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
  ; @param (namespaced map) query
  ;  {:namespace/id (string)(opt)}
  ; @param (namespaced map) document
  ; @param (map)(opt) options
  ;  {:prototype-f (function)(opt)}
  ;
  ; @usage
  ;  (mongo-db/update-document! "my-collection" {:namespace/score 100} {:namespace/score 0})
  ;
  ; @return (boolean)
  ([collection-name query document]
   (update-document! collection-name query document {}))

  ([collection-name query document options]
   (boolean (if-let [document (prepare-document collection-name document options)]
                    (if-let [query (adaptation/update-query query)]
                            (if-let [document (adaptation/update-input document)]
                                    (let [result (update! collection-name query document {:multi false :upsert false})]
                                         (mrt/updated-existing? result))))))))

; @usage
;  [:mongo-db/update-document! "my-collection" {:namespace/score 100} {:namespace/score 0}]
(a/reg-handled-fx :mongo-db/update-document! update-document!)



;; -- Updating document -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-documents!
  ; @param (string) collection-name
  ; @param (namespaced map) query
  ;  {:namespace/id (string)(opt)}
  ; @param (namespaced map) document
  ; @param (map)(opt) options
  ;  {:prototype-f (function)(opt)}
  ;
  ; @usage
  ;  (mongo-db/update-documents! "my-collection" {:namespace/score 100} {:namespace/score 0})
  ;
  ; @return (boolean)
  ([collection-name query document]
   (update-documents! collection-name query document {}))

  ([collection-name query document options]
   (boolean (if-let [document (prepare-document collection-name document options)]
                    (if-let [query (adaptation/update-query query)]
                            (if-let [document (adaptation/update-input document)]
                                    ; WARNING! DO NOT USE!
                                    ; java.lang.IllegalArgumentException: Replacements can not be multi
                                    (let [result (update! collection-name query document {:multi true :upsert false})]
                                         (mrt/updated-existing? result))))))))

; @usage
;  [:mongo-db/update-documents! "my-collection" {:namespace/score 100} {:namespace/score 0}]
(a/reg-handled-fx :mongo-db/update-documents! update-documents!)



;; -- Upserting document ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn upsert-document!
  ; @param (string) collection-name
  ; @param (namespaced map) query
  ; @param (namespaced map) document
  ; @param (map)(opt) options
  ;  {:prototype-f (function)(opt)}
  ;
  ; @usage
  ;  (mongo-db/upsert-document! "my-collection" {:namespace/score 100} {:namespace/score 0})
  ;
  ; @return (boolean)
  ([collection-name query document]
   (upsert-document! collection-name query document {}))

  ([collection-name query document options]
   (boolean (if-let [document (prepare-document collection-name document options)]
                    (if-let [query (adaptation/upsert-query query)]
                            (if-let [document (adaptation/upsert-input document)]
                                    (let [result (upsert! collection-name query document {:multi false})]
                                         (mrt/acknowledged? result))))))))

; @usage
;  [:mongo-db/upsert-document! "my-collection" {:namespace/score 100} {:namespace/score 0}]
(a/reg-handled-fx :mongo-db/upsert-document! upsert-document!)



;; -- Upserting documents -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn upsert-documents!
  ; @param (string) collection-name
  ; @param (namespaced map) query
  ; @param (namespaced map) document
  ; @param (map)(opt) options
  ;  {:prototype-f (function)(opt)}
  ;
  ; @usage
  ;  (mongo-db/upsert-documents! "my-collection" {:namespace/score 100} {:namespace/score 0})
  ;
  ; @return (boolean)
  ([collection-name query document]
   (upsert-documents! collection-name query document {}))

  ([collection-name query document options]
   (boolean (if-let [document (prepare-document collection-name document options)]
                    (if-let [query (adaptation/upsert-query query)]
                            (if-let [document (adaptation/upsert-input document)]
                                    ; WARNING! DO NOT USE!
                                    ; java.lang.IllegalArgumentException: Replacements can not be multi
                                    (let [result (upsert! collection-name query document {:multi true})]
                                         (mrt/acknowledged? result))))))))

; @usage
;  [:mongo-db/upsert-documents! "my-collection" {:namespace/score 100} {:namespace/score 0}]
(a/reg-handled-fx :mongo-db/upsert-documents! upsert-documents!)



;; -- Applying document -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn apply-document!
  ; @param (string) collection-name
  ; @param (namespaced map) document-id
  ; @param (function) f
  ; @param (map)(opt) options
  ;  {:prototype-f (function)(opt)}
  ;
  ; @usage
  ;  (mongo-db/apply-document! "my-collection" "MyObjectId" #(assoc % :color "Blue"))
  ;
  ; @return (namespaced map)
  ([collection-name document-id f]
   (apply-document! collection-name document-id f {}))

  ([collection-name document-id f options]
   (if-let [query (adaptation/apply-query document-id)]
           (if-let [document (reader/get-document-by-id collection-name document-id)]
                   (if-let [document (prepare-document collection-name document options)]
                           (if-let [document (-> document f adaptation/apply-input)]
                                   (let [result (update! collection-name query document {:multi false :upsert false})]
                                        (if (mrt/updated-existing? result)
                                            (return document)))))))))

; @usage
;  [:mongo-db/apply-document! "my-collection" "MyObjectId" #(assoc % :color "Blue")]
(a/reg-handled-fx :mongo-db/apply-document! apply-document!)



;; -- Removing document -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- remove-unordered-document!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (map) options
  ;
  ; @return (string)
  [collection-name document-id _]
  (if-let [document-id (adaptation/document-id-input document-id)]
          (let [result (remove-by-id! collection-name document-id)]
               (if (mrt/acknowledged? result)
                   (return document-id)))))

(defn- remove-ordered-document!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) collection-name
  ; @param (string) document-id
  ; @param (map) options
  ;
  ; @return (string)
  [collection-name document-id _]
  (if-let [document-id (adaptation/document-id-input document-id)]
          (do (reorder-following-documents! collection-name document-id {:operation :decrease})
              (let [result (remove-by-id! collection-name document-id)]
                   (if (mrt/acknowledged? result)
                       (return document-id))))))

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

  ([collection-name document-id {:keys [ordered?] :as options}]
   (if ordered? (remove-ordered-document!   collection-name document-id options)
                (remove-unordered-document! collection-name document-id options))))

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
  ;  (mongo-db/remove-documents! "my-collection" ["MyObjectId" "YourObjectId"])
  ;  =>
  ;  ["MyObjectId" "YourObjectId"]
  ;
  ; @return (string)
  ([collection-name document-ids]
   (remove-documents! collection-name document-ids {}))

  ([collection-name document-ids options]
   (vector/->items document-ids #(remove-document! collection-name % options))))

; @usage
;  [:mongo-db/remove-documents! "my-collection" ["MyObjectId" "YourObjectId"]]
(a/reg-handled-fx :mongo-db/remove-documents! remove-documents!)



;; -- Removing documents ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn remove-all-documents!
  ; @param (string) collection-name
  ;
  ; @usage
  ;  (mongo-db/remove-all-documents! "my-collection")
  ;
  ; @return (?)
  [collection-name]
  (drop! collection-name))

; @usage
;  [:mongo-db/remove-all-documents! "my-collection"]
(a/reg-handled-fx :mongo-db/remove-all-documents! remove-all-documents!)



;; -- Duplicating document ----------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING
; A get-document-copy-label függvény nincs használatban!
; Ha szükséges a dokumentumról készülő másolat címkéjét megjelölni másolat-jelzővel,
; akkor talán célszerűbb a "My document copy 1" kifejezés helyett a "My document #1" jelölést alkalmazni!
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
                               (adaptation/duplicate-output result))))))

(defn- duplicate-ordered-document!
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
                          (do (reorder-following-documents! collection-name document-id {:operation :increase})
                              (let [result (insert-and-return! collection-name document-copy)]
                                   (adaptation/duplicate-output result)))))))

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
   (duplicate-document! collection-name document-id {}))

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
  ;  (mongo-db/reorder-documents "my-collection" [["MyObjectId" 1] ["YourObjectId" 2]])
  ;
  ; @return (vectors in vector)
  [collection-name document-order]
  (let [namespace (reader/get-collection-namespace collection-name)
        order-key (keyword/add-namespace namespace :order)]
       (letfn [(f [[document-id document-dex]]
                  (if-let [document-id (adaptation/document-id-input document-id)]
                          (let [result (update! collection-name {:_id document-id}
                                                                {"$set" {order-key document-dex}})]
                               (if (mrt/acknowledged? result)
                                   (return [document-id document-dex])))))]
              (vector/->items document-order f))))
