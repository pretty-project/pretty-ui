
(ns pathom.universal
    (:require [mongo-db.api :as mongo-db]
      [pathom.env :as env]
      [pathom.register :as register]
      [x.server-core.api :as a]
      [com.wsscode.pathom3.connect.operation :as pco :refer [defresolver defmutation]]))



;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defresolver get-documents-by-pipeline
  ; @param (map) env
  ; @param (?) ?
  ;
  ; @return (map)
  ;  {:documents (map)
  ;    {:count (integer)
  ;     :result (maps in vector)}}
  [env _]
  {:documents (let [search-props    (env/env->search-props    env)
                    collection-name (env/env->collection-name env)]
                   {:count  (mongo-db/count-documents-by-pipeline collection-name search-props)
                    :result (mongo-db/get-documents-by-pipeline   collection-name search-props)})})

(defresolver get-document-by-id
  ; @param (map) env
  ; @param (?) ?
  ;
  ; @return (map)
  ;  {:document (map)}
  [env _]
  {:document (let [collection-name (env/env->param env :collection-name)
                   document-id     (env/env->param env :document-id)]
                  (mongo-db/get-document-by-id collection-name document-id))})



;; -- Mutations ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defmutation update-document!
  ; @param (map) ?
  ;  {:collection-name (string)
  ;   :document (map)
  ;   :ordered? (boolean)(opt)
  ;    Default: false}
  ;
  ; @return (map)
  ;  {:namespace/id (string)}
  [{:keys [collection-name document ordered?]}]
  {::pco/op-name 'universal/update-document!}
  (mongo-db/update-document! collection-name document {:ordered? ordered?}))

(defmutation remove-document!
  ; @param (map) ?
  ;  {:collection-name (string)
  ;   :document-id (map)}
  ;
  ; @return (map)
  ;  {:namespace/id (string)}
  [{:keys [collection-name document-id]}]
  {::pco/op-name 'universal/delete-document!}
  (mongo-db/remove-document! collection-name document-id))

(defmutation duplicate-document!
  ; @param (map) ?
  ;  {:collection-name (string)
  ;   :document-id (map)
  ;   :label-key (namespaced keyword)
  ;   :language-id (keyword)}
  ;
  ; @return (map)
  ;  {:namespace/id (string)}
  [{:keys [collection-name document-id label-key language-id]}]
  {::pco/op-name 'universal/duplicate-document!}
  (mongo-db/duplicate-document! collection-name document-id {:label-key   label-key
                                                             :language-id language-id}))

(defmutation reorder-documents!
  ; @param (map) ?
  ;  {:collection-name (string)
  ;   :document-order (vectors in vector)}
  ;
  ; @return (vectors in vector)
  [{:keys [collection-name document-order]}]
  {::pco/op-name 'universal/reorder-documents!}
  (mongo-db/reorder-documents! collection-name document-order))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (vector)
(def HANDLERS [get-documents-by-pipeline
               get-document-by-id
               update-document!
               remove-document!
               duplicate-document!
               reorder-documents!])

(register/reg-handlers! HANDLERS)
