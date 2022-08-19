
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-user.core.helpers
    (:require [mid-fruits.vector :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn user-roles->user-identified?
  ; @param (vector) user-roles
  ;
  ; @usage
  ;  (user/user-roles->user-identified? [...])
  ;
  ; @return (boolean)
  [user-roles]
  (vector/nonempty? user-roles))

(defn user-roles->user-unidentified?
  ; @param (vector) user-roles
  ;
  ; @usage
  ;  (user/user-roles->user-unidentified? [...])
  ;
  ; @return (boolean)
  [user-roles]
  (-> user-roles user-roles->user-identified? not))
